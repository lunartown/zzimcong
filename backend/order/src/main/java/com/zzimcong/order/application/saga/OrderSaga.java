package com.zzimcong.order.application.saga;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzimcong.order.application.dto.PaymentRequest;
import com.zzimcong.order.application.dto.PaymentResult;
import com.zzimcong.order.application.service.ProductService;
import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class OrderSaga {
    private final RedisTemplate<String, String> redisTemplate;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, PaymentRequest> kafkaTemplate;

    public OrderSaga(RedisTemplate<String, String> redisTemplate, OrderRepository orderRepository,
                     ProductService productService, ObjectMapper objectMapper, KafkaTemplate<String, PaymentRequest> kafkaTemplate) {
        this.redisTemplate = redisTemplate;
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    // 주문 준비 및 재고 예약
    public OrderPreparationResponse prepareOrderAndReserveInventory(Long userId, OrderPreparationRequest request) {
        boolean allReserved = reserveInventory(request.getOrderItems());
        if (allReserved) {
            Order tempOrder = createTempOrder(userId, request);
            String orderId = saveTempOrderToRedis(userId, tempOrder);
            return new OrderPreparationResponse(orderId, OrderPreparationStatus.SUCCESS);
        } else {
            return new OrderPreparationResponse(null, OrderPreparationStatus.INVENTORY_SHORTAGE);
        }
    }

    private String saveTempOrderToRedis(Long userId, Order tempOrder) {
        String orderId = generateOrderId();
        String key = "temp_order:" + userId + ":" + orderId;
        String orderJson = objectMapper.writeValueAsString(tempOrder);
        redisTemplate.opsForValue().set(key, orderJson, Duration.ofMinutes(30));
        return orderId;
    }

    // 결제 요청
    public void requestPayment(Long userId, String orderId, OrderConfirmationRequest request) {
        String key = "temp_order:" + userId + ":" + orderId;
        String orderJson = redisTemplate.opsForValue().get(key);
        if (orderJson == null) {
            throw new OrderNotFoundException("임시 주문을 찾을 수 없습니다.");
        }

        Order order = objectMapper.readValue(orderJson, Order.class);
        order.updateOrderDetails(request);

        // 결제 요청
        PaymentRequest paymentRequest = new PaymentRequest(order.getId(), order.getTotalAmount());
        kafkaTemplate.send("payment-requests", paymentRequest);

        // 결제 요청 후 Redis의 만료 시간을 연장 (결제 완료 대기)
        redisTemplate.expire(key, Duration.ofMinutes(15));
    }

    // 결제 결과 처리
    @KafkaListener(topics = "payment-results")
    public void handlePaymentResult(PaymentResult result) {
        String key = "temp_order:" + result.getUserId() + ":" + result.getOrderId();
        String orderJson = redisTemplate.opsForValue().get(key);

        if (orderJson == null) {
            log.warn("Payment result received for non-existent order: {}", result.getOrderId());
            return;
        }

        Order order = objectMapper.readValue(orderJson, Order.class);

        if (result.isSuccess()) {
            order.setStatus(OrderStatus.PAYMENT_COMPLETED);
            orderRepository.save(order);  // DB에 저장
            redisTemplate.delete(key);  // Redis에서 임시 주문 정보 삭제
            initiateShipment(order);
        } else {
            compensateOrderFailure(order, "결제 실패");
        }
    }

    private void compensateOrderFailure(Order order, String reason) {
        // 재고 원복
        order.getOrderItems().forEach(item ->
                productService.releaseInventory(item.getProductId(), item.getQuantity()));

        // Redis에서 임시 주문 정보 삭제
        String key = "temp_order:" + order.getUserId() + ":" + order.getId();
        redisTemplate.delete(key);

        log.info("Order {} failed: {}", order.getId(), reason);
    }

    private void initiateShipment(Order order) {
        // 배송 시작 로직
        kafkaTemplate.send("shipment-requests", new ShipmentRequest(order.getId(), order.getShippingAddress()));
    }
}