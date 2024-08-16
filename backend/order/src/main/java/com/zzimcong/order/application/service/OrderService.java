package com.zzimcong.order.application.service;

import com.zzimcong.order.application.dto.OrderItemRequest;
import com.zzimcong.order.application.dto.OrderRequest;
import com.zzimcong.order.domain.entity.*;
import com.zzimcong.order.domain.repository.OrderRepository;
import com.zzimcong.order.infrastructure.kafka.KafkaProducer;
import com.zzimcong.order.infrastructure.kafka.PaymentEvent;
import com.zzimcong.order.infrastructure.statemachine.OrderStateMachineFactory;
import com.zzimcong.zzimconginventorycore.InventoryEvent;
import com.zzimcong.zzimconginventorycore.InventoryEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final KafkaProducer kafkaProducer;
    private final OrderStateMachineFactory stateMachineFactory;

    @Value("${kafka.topic.inventory-events}")
    private String inventoryTopic;

    @Value("${kafka.topic.payment-events}")
    private String paymentTopic;

    @Value("${kafka.topic.order-events}")
    private String orderTopic;

    @Value("${order.auto-confirm.days}")
    private int autoConfirmDays;

    @Value("${order.auto-refund.days}")
    private int autoRefundDays;

    public OrderService(OrderRepository orderRepository,
                        KafkaProducer kafkaProducer,
                        OrderStateMachineFactory stateMachineFactory) {
        this.orderRepository = orderRepository;
        this.kafkaProducer = kafkaProducer;
        this.stateMachineFactory = stateMachineFactory;
    }

    /**
     * 주문을 생성하고 재고 확인 요청을 보냅니다.
     */
    @Transactional
    public Order createOrder(OrderRequest request) {
        logger.info("Creating new order for customer: {}", request.getName());

        // 주문 생성
        Order order = new Order();
        order.setState(OrderState.CREATED);
        order.setName(request.getName());
        order.setAddr(request.getAddr());
        order.setAddrDetail(request.getAddrDetail());
        order.setZipcode(request.getZipcode());
        order.setPhone(request.getPhone());
        order.setMessage(request.getMessage());
        order.setOrderAmount(request.getOrderAmount());
        order.setPaymentAmount(request.getPaymentAmount());
        order.setPayment(request.getPayment());

        // 주문 아이템 추가
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(itemRequest.getProductId());
            item.setQuantity(itemRequest.getQuantity());
            item.setPrice(itemRequest.getPrice());
            orderItems.add(item);

            // 재고 확인 요청
            kafkaProducer.send(inventoryTopic, new InventoryEvent(itemRequest.getProductId(), InventoryEventType.CHECK, itemRequest.getQuantity()));
        }
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        logger.info("Order created with ID: {}", savedOrder.getId());

        // 결제 처리 요청
        kafkaProducer.send(paymentTopic, new PaymentEvent(savedOrder.getId(), savedOrder.getPaymentAmount(), request.getPayment()));

        return savedOrder;
    }

    //재고 이벤트
    @KafkaListener(topics = "${kafka.topic.inventory-events}", groupId = "order-group")
    @Transactional
    public void handleInventoryEvent(InventoryEvent event) {
        logger.info("Received inventory event: {}", event);
        Order order = orderRepository.findByProductIdAndQuantity(event.getProductId(), event.getQuantity())
                .orElseThrow(() -> {
                    logger.error("Order not found for product ID: {} and quantity: {}", event.getProductId(), event.getQuantity());
                    return new RuntimeException("주문을 찾을 수 없습니다.");
                });

        var sm = stateMachineFactory.create(order);

        if (event.getEventType() == InventoryEventType.CONFIRM) {
            if (sm.sendEvent(OrderEventType.RESERVE_STOCK)) {
                order.setState(OrderState.STOCK_RESERVED);
                orderRepository.save(order);
                // 결제 처리 요청
                kafkaProducer.send(paymentTopic, new PaymentEvent(order.getId(), order.getPaymentAmount(), order.getPayment()));
                logger.info("Stock reserved for order: {}. Payment processing initiated.", order.getId());
            }
        } else {
            logger.warn("Insufficient stock for order: {}. Cancelling order.", order.getId());
            cancelOrder(order);
        }
    }

    //결제 처리
    @KafkaListener(topics = "${kafka.topic.payment-events}", groupId = "order-group")
    @Transactional
    public void handlePaymentEvent(PaymentEvent event) {
        logger.info("Received payment event: {}", event);
        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> {
                    logger.error("Order not found for ID: {}", event.getOrderId());
                    return new RuntimeException("주문을 찾을 수 없습니다.");
                });

        var sm = stateMachineFactory.create(order);

        if (event.isSuccess()) {
            if (sm.sendEvent(OrderEventType.PROCESS_PAYMENT)) {
                order.setState(OrderState.PAYMENT_PROCESSED);
                orderRepository.save(order);
                kafkaProducer.send(orderTopic, new OrderEvent(order.getId(), OrderEventType.COMPLETE_ORDER));
                logger.info("Payment processed for order: {}. Order completed.", order.getId());
            }
        } else {
            logger.warn("Payment failed for order: {}. Cancelling order.", order.getId());
            cancelOrder(order);
        }
    }

    //주문 취소
    @Transactional
    public void cancelOrder(Order order) {
        logger.info("Cancelling order: {}", order.getId());
        var sm = stateMachineFactory.create(order);
        if (sm.sendEvent(OrderEventType.CANCEL_ORDER)) {
            order.setState(OrderState.CANCELED);
            orderRepository.save(order);
            kafkaProducer.send(orderTopic, new OrderEvent(order.getId(), OrderEventType.CANCEL_ORDER));

            // 각 주문 항목에 대해 재고 복원 요청
            for (OrderItem item : order.getOrderItems()) {
                kafkaProducer.send(inventoryTopic, new InventoryEvent(item.getProductId(), InventoryEventType.RESTORE, item.getQuantity()));
                logger.info("Stock restore requested for product: {}, quantity: {}", item.getProductId(), item.getQuantity());
            }

            logger.info("Order {} cancelled and stock restore requested for all items.", order.getId());
        }
    }

    //배송 준비 중
    @Transactional
    public void prepareShipment(Long orderId) {
        logger.info("Preparing shipment for order: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Order not found for ID: {}", orderId);
                    return new RuntimeException("주문을 찾을 수 없습니다.");
                });

        if (order.getState() == OrderState.ORDER_COMPLETED) {
            var sm = stateMachineFactory.create(order);
            if (sm.sendEvent(OrderEventType.PREPARE_SHIPMENT)) {
                order.setState(OrderState.PREPARING_FOR_SHIPMENT);
                orderRepository.save(order);
                logger.info("Order {} is now in PREPARING_FOR_SHIPMENT state.", orderId);
            }
        } else {
            logger.warn("Invalid order state for preparing shipment. Order ID: {}, Current State: {}", orderId, order.getState());
            throw new IllegalStateException("잘못된 주문 상태입니다.");
        }
    }

    //배송 중
    @Transactional
    public void shipOrder(Long orderId) {
        logger.info("Shipping order: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Order not found for ID: {}", orderId);
                    return new RuntimeException("주문을 찾을 수 없습니다.");
                });

        if (order.getState() == OrderState.PREPARING_FOR_SHIPMENT) {
            var sm = stateMachineFactory.create(order);
            if (sm.sendEvent(OrderEventType.SHIP_ORDER)) {
                order.setState(OrderState.SHIPPING);
                orderRepository.save(order);
                logger.info("Order {} is now in SHIPPING state.", orderId);
            }
        } else {
            logger.warn("Invalid order state for preparing shipment. Order ID: {}, Current State: {}", orderId, order.getState());
            throw new IllegalStateException("잘못된 주문 상태입니다.");
        }
    }

    //배송 완료
    @Transactional
    public void deliverOrder(Long orderId) {
        logger.info("Delivering order: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Order not found for ID: {}", orderId);
                    return new RuntimeException("주문을 찾을 수 없습니다.");
                });

        if (order.getState() == OrderState.SHIPPING) {
            var sm = stateMachineFactory.create(order);
            if (sm.sendEvent(OrderEventType.DELIVER_ORDER)) {
                order.setState(OrderState.DELIVERED);
                orderRepository.save(order);
                logger.info("Order {} is now in DELIVERED state.", orderId);
            }
        } else {
            logger.warn("Invalid order state for preparing shipment. Order ID: {}, Current State: {}", orderId, order.getState());
            throw new IllegalStateException("잘못된 주문 상태입니다.");
        }
    }

    //주문 확정
    @Scheduled(fixedDelayString = "${order.auto-confirm.check-interval}")
    @Transactional
    public void checkAndConfirmOrders() {
        logger.info("Starting automatic order confirmation process");
        LocalDateTime confirmationDate = LocalDateTime.now().minusDays(autoConfirmDays);
        List<Order> ordersToConfirm = orderRepository.findByStateAndDeliveredAtBefore(OrderState.DELIVERED, confirmationDate);

        for (Order order : ordersToConfirm) {
            var sm = stateMachineFactory.create(order);
            if (sm.sendEvent(OrderEventType.CONFIRM_ORDER)) {
                order.setState(OrderState.ORDER_CONFIRMED);
                orderRepository.save(order);
                logger.info("Order {} automatically confirmed.", order.getId());
            }
        }
        logger.info("Completed automatic order confirmation process. Confirmed {} orders.", ordersToConfirm.size());
    }


    //환불 요청 처리
    @Scheduled(fixedDelayString = "${order.auto-refund.check-interval}")
    @Transactional
    public void checkAndCompleteRefunds() {
        logger.info("Starting automatic refund completion process");
        LocalDateTime refundDate = LocalDateTime.now().minusDays(autoRefundDays);
        List<Order> ordersToRefund = orderRepository.findByStateAndRefundRequestedAtBefore(OrderState.REFUND_REQUESTED, refundDate);

        for (Order order : ordersToRefund) {
            var sm = stateMachineFactory.create(order);
            if (sm.sendEvent(OrderEventType.COMPLETE_REFUND)) {
                order.setState(OrderState.REFUND_COMPLETED);
                orderRepository.save(order);

                // 각 주문 항목에 대해 재고 복원 요청
                for (OrderItem item : order.getOrderItems()) {
                    kafkaProducer.send(inventoryTopic, new InventoryEvent(item.getProductId(), InventoryEventType.RESTORE, item.getQuantity()));
                    logger.info("Stock restore requested for product: {}, quantity: {}", item.getProductId(), item.getQuantity());
                }

                logger.info("Refund completed for order: {}. Stock restore requested for all items.", order.getId());
            }
        }
        logger.info("Completed automatic refund process. Processed {} refunds.", ordersToRefund.size());
    }
}