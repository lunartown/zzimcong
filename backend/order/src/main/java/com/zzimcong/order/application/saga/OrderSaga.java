package com.zzimcong.order.application.saga;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzimcong.order.application.dto.*;
import com.zzimcong.order.application.mapper.OrderRequestMapper;
import com.zzimcong.order.application.service.OrderService;
import com.zzimcong.order.application.service.ProductService;
import com.zzimcong.order.common.exception.ErrorCode;
import com.zzimcong.order.common.exception.InternalServerErrorException;
import com.zzimcong.order.common.exception.NotFoundException;
import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderItem;
import com.zzimcong.order.domain.entity.OrderStatus;
import com.zzimcong.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j(topic = "order-saga")
@RequiredArgsConstructor
public class OrderSaga {
    private final OrderService orderService;
    private final ProductService productService;
    private final OrderRequestMapper orderRequestMapper;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, PaymentRequest> kafkaTemplate;


    /***************************************************************************
     주문 준비 프로세스(재고 예약, 임시 주문 생성, 임시 주문 정보 Redis에 저장)
     ***************************************************************************/

    // 주문 준비
    public OrderPreparationResponse prepareOrder(Long userId, OrderPreparationRequest request) {
        boolean allReserved = reserveInventory(request);
        if (allReserved) {
            Order tempOrder = createTempOrder(userId, request);
            String orderUuid = saveTempOrderToRedis(userId, tempOrder);
            return new OrderPreparationResponse(orderUuid, OrderPreparationStatus.SUCCESS);
        } else {
            return new OrderPreparationResponse(null, OrderPreparationStatus.INVENTORY_SHORTAGE);
        }
    }

    // 재고 예약
    public boolean reserveInventory(OrderPreparationRequest request) {
        return request.items().stream()
                .allMatch(item -> productService.reserveInventory(item.productId(), item.quantity()));
    }

    // 임시 주문 생성
    public Order createTempOrder(Long userId, OrderPreparationRequest request) {
        log.info("Creating temporary order for user ID: {}", userId);

        Order tempOrder = orderRequestMapper.orderPreperationRequestToOrder(request);
        tempOrder.setUserId(userId);

        return tempOrder;
    }

    // 임시 주문 정보 Redis에 저장
    private String saveTempOrderToRedis(Long userId, Order tempOrder) {
        String orderUuid = generateOrderUuid();
        String key = "temp_order:" + userId + ":" + orderUuid;

        try {
            String orderJson = objectMapper.writeValueAsString(tempOrder);
            redisTemplate.opsForValue().set(key, orderJson, Duration.ofMinutes(30));
            log.info("Temporary order created with ID: {}", orderUuid);
        } catch (JsonProcessingException e) {
            log.error("Error creating temporary order", e);
            throw new RuntimeException("임시 주문 생성 중 오류가 발생했습니다.");
        }

        return orderUuid;
    }

    private String generateOrderUuid() {
        return UUID.randomUUID().toString();
    }

    /***************************************************************************
     주문 생성 프로세스(주문 정보 업데이트, 결제 요청)
     ***************************************************************************/

    // 주문 생성
    public void createOrder(Long userId, String uuid, OrderCreationRequest request) {
        Order order = updateOrderDetails(userId, uuid, request);
        processPayment(userId, uuid, request.paymentDetailsRequest(), order);
    }

    // 주문 정보 업데이트
    public Order updateOrderDetails(Long userId, String uuid, OrderCreationRequest orderCreationRequest) {
        String key = "temp_order:" + userId + ":" + uuid;

        return (Order) redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.watch(key);
                String orderJson = (String) operations.opsForValue().get(key);

                if (orderJson == null) {
                    throw new NotFoundException(ErrorCode.TEMP_ORDER_NOT_FOUND, "uuid : " + uuid);
                }

                try {
                    operations.multi();

                    Order order = objectMapper.readValue(orderJson, Order.class);
                    orderRequestMapper.updateOrderFromRequest(orderCreationRequest, order);
                    order.setStatus(OrderStatus.PAYMENT_REQUESTED);

                    String updatedOrderJson = objectMapper.writeValueAsString(order);
                    operations.opsForValue().set(key, updatedOrderJson);
                    operations.expire(key, Duration.ofMinutes(15));

                    List<Object> txResults = operations.exec();

                    if (txResults == null || txResults.isEmpty()) {
                        throw new ConcurrentModificationException("주문 정보가 동시에 수정되었습니다. 다시 시도해주세요.");
                    }

                    log.info("Order details updated successfully for order: {}", uuid);
                    return order;

                } catch (JsonProcessingException e) {
                    operations.discard();
                    log.error("Error updating temporary order", e);
                    throw new InternalServerErrorException(ErrorCode.ORDER_UPDATE_FAILED);
                }
            }
        });
    }

    // 결제 요청
    public void processPayment(Long userId, String uuid, PaymentDetailsRequest request, Order order) {
        // 결제 요청
        PaymentRequest paymentRequest = new PaymentRequest(userId, uuid, request, order.getPaymentAmount());
        kafkaTemplate.send("payment-requests", paymentRequest);

        log.info("Order confirmed and payment requested");
    }

    // 결제 결과 처리
    @KafkaListener(topics = "payment-results")
    public void handlePaymentResult(PaymentResponse result) {
        String key = "temp_order:" + result.getUserId() + ":" + result.getUuid();
        String orderJson = redisTemplate.opsForValue().get(key);

        if (orderJson == null) {
            log.warn("Payment result received for non-existent order: {}", result.getUuid());
            return;
        }
        try {
            Order order = objectMapper.readValue(orderJson, Order.class);

            if (result.isSuccess()) {
                order.setStatus(OrderStatus.ORDER_COMPLETED);
                orderRepository.save(order);  // DB에 저장
                redisTemplate.delete(key);  // Redis에서 임시 주문 정보 삭제
            } else {
                compensatePaymentFailure(order);
                redisTemplate.delete(key);
            }
        } catch (JsonProcessingException e) {
            log.error("Error processing payment result", e);
            throw new InternalServerErrorException(ErrorCode.PAYMENT_RESULT_PROCESSING_FAILED);
        }
    }

    // 보상 트랜잭션: 결제 실패 시 재고 회수
    private void compensatePaymentFailure(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            productService.releaseInventory(item.getProductId(), item.getQuantity());
        }
    }

//    public void prepareShipping(String uuid) {
//        if (initiateShipment(uuid)) {
//            orderService.completeOrder(uuid);
//        } else {
//            compensateShipmentFailure(uuid);
//            throw new ShipmentFailedException("배송 시작에 실패했습니다.");
//        }
//    }
//
//    private boolean reserveInventory(OrderPreparationRequest request) {
//        return orderService.reserveInventory(request);
//    }
//
//    private boolean initiateShipment(String uuid) {
//        return shippingService.initiateShipment(uuid);
//    }
//
//
//    private void compensateShipmentFailure(String uuid) {
//        paymentService.refundPayment(uuid);
//        productService.releaseInventory(uuid);
//        orderService.cancelOrder(uuid);
//    }
}