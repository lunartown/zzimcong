package com.zzimcong.order.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzimcong.order.application.mapper.OrderRequestMapper;
import com.zzimcong.order.application.mapper.PaymentDetailsMapper;
import com.zzimcong.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j(topic = "order-service")
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final OrderRequestMapper orderRequestMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final PaymentDetailsMapper paymentDetailsMapper;

//    // 주문 준비 및 재고 예약
//    public OrderPreparationResponse prepareOrderAndReserveInventory(Long userId, OrderPreparationRequest request) {
//        boolean allReserved = reserveInventory(request);
//        if (allReserved) {
//            Order tempOrder = createTempOrder(userId, request);
//            String orderUuid = saveTempOrderToRedis(userId, tempOrder);
//            return new OrderPreparationResponse(orderUuid, OrderPreparationStatus.SUCCESS);
//        } else {
//            return new OrderPreparationResponse(null, OrderPreparationStatus.INVENTORY_SHORTAGE);
//        }
//    }
//
//    // 재고 예약
//    public boolean reserveInventory(OrderPreparationRequest request) {
//        return request.items().stream()
//                .allMatch(item -> productService.reserveInventory(item.productId(), item.quantity()));
//    }

//    // 임시 주문 생성
//    public Order createTempOrder(Long userId, OrderPreparationRequest request) {
//        log.info("Creating temporary order for user ID: {}", userId);
//
//        Order tempOrder = orderRequestMapper.orderPreperationRequestToOrder(request);
//        tempOrder.setUserId(userId);
//
//        return tempOrder;
//    }
//
//    // 임시 주문 정보 Redis에 저장
//    private String saveTempOrderToRedis(Long userId, Order tempOrder) {
//        String orderUuid = generateOrderUuid();
//        String key = "temp_order:" + userId + ":" + orderUuid;
//
//        try {
//            String orderJson = objectMapper.writeValueAsString(tempOrder);
//            redisTemplate.opsForValue().set(key, orderJson, Duration.ofMinutes(30));
//            log.info("Temporary order created with ID: {}", orderUuid);
//        } catch (JsonProcessingException e) {
//            log.error("Error creating temporary order", e);
//            throw new RuntimeException("임시 주문 생성 중 오류가 발생했습니다.");
//        }
//
//        return orderUuid;
//    }
//
//    private String generateOrderUuid() {
//        return UUID.randomUUID().toString();
//    }

//    public Order updateOrderDetails(Long userId, String uuid, OrderCreationRequest orderCreationRequest) {
//        String key = "temp_order:" + userId + ":" + uuid;
//
//        return (Order) redisTemplate.execute(new SessionCallback<Object>() {
//            @Override
//            public Object execute(RedisOperations operations) throws DataAccessException {
//                operations.watch(key);
//                String orderJson = (String) operations.opsForValue().get(key);
//
//                if (orderJson == null) {
//                    throw new NotFoundException(ErrorCode.TEMP_ORDER_NOT_FOUND, "uuid : " + uuid);
//                }
//
//                try {
//                    operations.multi();
//
//                    Order order = objectMapper.readValue(orderJson, Order.class);
//                    orderRequestMapper.updateOrderFromRequest(orderCreationRequest, order);
//                    order.setStatus(OrderStatus.PAYMENT_REQUESTED);
//
//                    String updatedOrderJson = objectMapper.writeValueAsString(order);
//                    operations.opsForValue().set(key, updatedOrderJson);
//                    operations.expire(key, Duration.ofMinutes(15));
//
//                    List<Object> txResults = operations.exec();
//
//                    if (txResults == null || txResults.isEmpty()) {
//                        throw new ConcurrentModificationException("주문 정보가 동시에 수정되었습니다. 다시 시도해주세요.");
//                    }
//
//                    log.info("Order details updated successfully for order: {}", uuid);
//                    return order;
//
//                } catch (JsonProcessingException e) {
//                    operations.discard();
//                    log.error("Error updating temporary order", e);
//                    throw new InternalServerErrorException(ErrorCode.ORDER_UPDATE_FAILED);
//                }
//            }
//        });
//    }


//    public void processPayment(PaymentDetailsRequest request, Order order) { // 결제 요청
//        try {
//            // 결제 요청
//            PaymentRequest paymentRequest = new PaymentRequest(request, order.getPaymentAmount());
//            kafkaTemplate.send("payment-requests", paymentRequest);
//
//            log.info("Order confirmed and payment requested");
//        } catch (JsonProcessingException e) {
//            log.error("Error processing temporary order", e);
//            throw new RuntimeException("주문 처리 중 오류가 발생했습니다.");
//        }
//    }

//    @Transactional
//    public void completeOrder(PaymentResponse paymentResponse) {
//        String key = "temp_order:" + paymentResponse.getUserId() + ":" + paymentResponse.getOrderId();
//        String orderJson = redisTemplate.opsForValue().get(key);
//
//        if (orderJson == null) {
//            log.error("Temporary order not found for payment result: {}", paymentResponse);
//            return;
//        }
//
//        try {
//            Order order = objectMapper.readValue(orderJson, Order.class);
//            order.setStatus(OrderStatus.PAYMENT_COMPLETED);
//            Order savedOrder = orderRepository.save(order);
//            redisTemplate.delete(key);
//
//            log.info("Order completed and saved to DB. Order ID: {}", savedOrder.getId());
//        } catch (JsonProcessingException e) {
//            log.error("Error completing order", e);
//            throw new RuntimeException("주문 완료 중 오류가 발생했습니다.");
//        }
//    }


//    // 결제 요청
//    public void requestPayment(Long userId, String orderId, OrderCreationRequest request) {
//        String key = "temp_order:" + userId + ":" + orderId;
//        String orderJson = redisTemplate.opsForValue().get(key);
//        if (orderJson == null) {
//            throw new OrderNotFoundException("임시 주문을 찾을 수 없습니다.");
//        }
//
//        Order order = objectMapper.readValue(orderJson, Order.class);
//        order.updateOrderDetails(request);
//
//        // 결제 요청
//        PaymentRequest paymentRequest = new PaymentRequest(order.getId(), order.getTotalAmount());
//        kafkaTemplate.send("payment-requests", paymentRequest);
//
//        // 결제 요청 후 Redis의 만료 시간을 연장 (결제 완료 대기)
//        redisTemplate.expire(key, Duration.ofMinutes(15));
//    }

    // 결제 결과 처리
//    @KafkaListener(topics = "payment-results")
//    public void handlePaymentResult(PaymentResponse result) {
//        String key = "temp_order:" + result.getUserId() + ":" + result.getOrderId();
//        String orderJson = redisTemplate.opsForValue().get(key);
//
//        if (orderJson == null) {
//            log.warn("Payment result received for non-existent order: {}", result.getOrderId());
//            return;
//        }
//
//        Order order = objectMapper.readValue(orderJson, Order.class);
//
//        if (result.isSuccess()) {
//            order.setStatus(OrderStatus.PAYMENT_COMPLETED);
//            orderRepository.save(order);  // DB에 저장
//            redisTemplate.delete(key);  // Redis에서 임시 주문 정보 삭제
//            initiateShipment(order);
//        } else {
//            compensateOrderFailure(order, "결제 실패");
//        }
//    }

//    private void compensateOrderFailure(Order order, String reason) {
//        // 재고 원복
//        order.getOrderItems().forEach(item ->
//                productService.releaseInventory(item.getProductId(), item.getQuantity()));
//
//        // Redis에서 임시 주문 정보 삭제
//        String key = "temp_order:" + order.getUserId() + ":" + order.getId();
//        redisTemplate.delete(key);
//
//        log.info("Order {} failed: {}", order.getId(), reason);
//    }
//
//    private void initiateShipment(Order order) {
//        // 배송 시작 로직
//        kafkaTemplate.send("shipment-requests", new ShipmentRequest(order.getId(), order.getShippingAddress()));
//    }
//
//    private String generateOrderId() {
//        // 주문 ID 생성 로직 구현
//        return UUID.randomUUID().toString();
//    }
//
//    @Transactional
//    public void updateOrderDetails(Long userId, String orderId, OrderConfirmationRequest confirmationRequest) {
//        String key = "temp_order:" + userId + ":" + orderId;
//        String orderJson = redisTemplate.opsForValue().get(key);
//
//        if (orderJson == null) {
//            log.error("Temporary order not found for user ID: {} and order ID: {}", userId, orderId);
//            throw new RuntimeException("임시 주문을 찾을 수 없습니다.");
//        }
//
//        Order order = objectMapper.readValue(orderJson, Order.class);
//        order.updateOrderDetails(confirmationRequest);
//        order.setStatus(OrderStatus.PAYMENT_PENDING);
//
//        redisTemplate.expire(key, Duration.ofMinutes(15));
//    }
//            // 결제 요청
//            paymentService.requestPayment(order);
//
//            // Redis의 만료 시간을 연장 (결제 완료 대기)
//
//            log.info("Order confirmed and payment requested for order ID: {}", orderId);
//        } catch (JsonProcessingException e) {
//            log.error("Error processing temporary order", e);
//            throw new RuntimeException("주문 처리 중 오류가 발생했습니다.");
//        }
//    }
//
//    @Transactional
//    public void completeOrder(PaymentResult paymentResult) {
//        String key = "temp_order:" + paymentResult.getUserId() + ":" + paymentResult.getOrderId();
//        String orderJson = redisTemplate.opsForValue().get(key);
//
//        if (orderJson == null) {
//            log.error("Temporary order not found for payment result: {}", paymentResult);
//            return;
//        }
//
//        try {
//            Order order = objectMapper.readValue(orderJson, Order.class);
//            order.setStatus(OrderStatus.PAYMENT_COMPLETED);
//            Order savedOrder = orderRepository.save(order);
//            redisTemplate.delete(key);
//
//            log.info("Order completed and saved to DB. Order ID: {}", savedOrder.getId());
//        } catch (JsonProcessingException e) {
//            log.error("Error completing order", e);
//            throw new RuntimeException("주문 완료 중 오류가 발생했습니다.");
//        }
//    }
//
//    @Transactional
//    public void cancelOrder(PaymentResult paymentResult) {
//        String key = "temp_order:" + paymentResult.getUserId() + ":" + paymentResult.getOrderId();
//        String orderJson = redisTemplate.opsForValue().get(key);
//
//        if (orderJson == null) {
//            log.error("Temporary order not found for failed payment: {}", paymentResult);
//            return;
//        }
//
//        try {
//            Order order = objectMapper.readValue(orderJson, Order.class);
//            order.getOrderItems().forEach(item ->
//                    productService.releaseInventory(item.getProductId(), item.getQuantity()));
//            redisTemplate.delete(key);
//
//            log.info("Order canceled due to payment failure. Order ID: {}", paymentResult.getOrderId());
//        } catch (JsonProcessingException e) {
//            log.error("Error canceling order", e);
//            throw new RuntimeException("주문 취소 중 오류가 발생했습니다.");
//        }
//    }
//
//    @KafkaListener(topics = "shipping-events")
//    @Transactional
//    public void handleShippingEvent(ShippingEvent event) {
//        log.info("Received shipping event for order ID: {}, Event type: {}", event.getOrderId(), event.getEventType());
//        Order order = getOrder(event.getOrderId());
//
//        OrderStatus newStatus = switch (event.getEventType()) {
//            case PREPARING -> OrderStatus.PREPARING_FOR_SHIPMENT;
//            case SHIPPING -> OrderStatus.SHIPPING;
//            case DELIVERED -> OrderStatus.DELIVERED;
//        };
//
//        updateOrderStatus(order, newStatus);
//        log.info("Updated order status: {}, Order ID: {}", newStatus, order.getId());
//    }
//
//
//    @Transactional
//    public void updateOrderStatus(Order order, OrderStatus status) {
//        log.info("Updating order status. Order ID: {}, New status: {}", order.getId(), status);
//        order.setStatus(status);
//        orderRepository.save(order);
//    }
//
//    public Order getOrder(Long orderId) {
//        log.debug("Fetching order with ID: {}", orderId);
//        return orderRepository.findById(orderId)
//                .orElseThrow(() -> {
//                    log.error("Order not found with ID: {}", orderId);
//                    return new RuntimeException("주문을 찾을 수 없습니다: " + orderId);
//                });
//    }
//
//    public OrderResponse getOrderResponse(Long orderId) {
//        log.debug("Fetching order response for order ID: {}", orderId);
//        Order order = getOrder(orderId);
//        return orderMapper.orderToOrderResponse(order);
//    }
//
//    public Page<OrderResponse> getOrderList(Pageable pageable) {
//        log.debug("Fetching order list. Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
//        Page<Order> orderPage = orderRepository.findAll(pageable);
//        return orderPage.map(orderMapper::orderToOrderResponse);
//    }
//
//    public Page<OrderResponse> getUserOrders(Long userId, Pageable pageable) {
//        log.debug("Fetching orders for user ID: {}. Page: {}, Size: {}", userId, pageable.getPageNumber(), pageable.getPageSize());
//        Page<Order> ordersWithItems = orderRepository.findByUserId(userId, pageable);
//        return ordersWithItems.map(orderMapper::orderToOrderResponse);
//    }
//
//    @Transactional
//    public void requestRefund(Long orderId) {
//        log.info("Requesting refund for order ID: {}", orderId);
//        Order order = getOrder(orderId);
//        updateOrderStatus(order, OrderStatus.REFUND_REQUESTED);
//    }
//
//    @Scheduled(fixedDelay = 86400000) // 매일 실행
//    public void autoConfirmOrders() {
//        log.info("Starting auto-confirmation of orders");
//        List<Order> ordersToConfirm = getOrdersToConfirm();
//        log.info("Found {} orders to auto-confirm", ordersToConfirm.size());
//
//        for (Order order : ordersToConfirm) {
//            try {
//                confirmOrder(order);
//            } catch (Exception e) {
//                log.error("Error during auto-confirmation of order ID: {}", order.getId(), e);
//            }
//        }
//    }
//
//    public List<Order> getOrdersToConfirm() {
//        LocalDateTime confirmationDate = LocalDateTime.now().minusDays(1);
//        log.debug("Fetching orders to confirm before: {}", confirmationDate);
//        return orderRepository.findByStatusAndDeliveredAtBefore(OrderStatus.DELIVERED, confirmationDate);
//    }
//
//    @Transactional
//    public void confirmOrder(Order order) {
//        log.info("Confirming order with ID: {}", order.getId());
//        updateOrderStatus(order, OrderStatus.ORDER_CONFIRMED);
//    }
//
//    @Scheduled(fixedDelay = 86400000) // 매일 실행
//    public void autoRefundOrders() {
//        log.info("Starting auto-refund of orders");
//        List<Order> ordersToRefund = getOrdersToRefund();
//        log.info("Found {} orders to auto-refund", ordersToRefund.size());
//
//        for (Order order : ordersToRefund) {
//            try {
//                refundOrder(order);
//            } catch (Exception e) {
//                log.error("Error during auto-refund of order ID: {}", order.getId(), e);
//            }
//        }
//    }
//
//    public List<Order> getOrdersToRefund() {
//        LocalDateTime refundDate = LocalDateTime.now().minusDays(1);
//        log.debug("Fetching orders to refund before: {}", refundDate);
//        return orderRepository.findByStatusAndDeliveredAtBefore(OrderStatus.REFUND_REQUESTED, refundDate);
//    }
//
//    @Transactional
//    public void refundOrder(Order order) {
//        log.info("Processing refund for order ID: {}", order.getId());
//        updateOrderStatus(order, OrderStatus.REFUND_COMPLETED);
//        paymentService.refundPayment(order);
//    }
//
//    public boolean isOrderOwnedByUser(Long orderId, Long userId) {
//        log.debug("Checking if order ID: {} is owned by user ID: {}", orderId, userId);
//        Order order = getOrder(orderId);
//        return order.getUserId().equals(userId);
//    }

}