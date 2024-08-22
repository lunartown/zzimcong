package com.zzimcong.order.application.service;

import com.zzimcong.order.application.dto.OrderRequest;
import com.zzimcong.order.application.dto.OrderResponse;
import com.zzimcong.order.application.mapper.OrderMapper;
import com.zzimcong.order.application.mapper.OrderRequestMapper;
import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderStatus;
import com.zzimcong.order.domain.repository.OrderRepository;
import com.zzimcong.zzimconginventorycore.common.event.ShippingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j(topic = "order-service")
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final ProductService productService;
    private final OrderMapper orderMapper;
    private final OrderRequestMapper orderRequestMapper;

    @KafkaListener(topics = "shipping-events")
    @Transactional
    public void handleShippingEvent(ShippingEvent event) {
        log.info("Received shipping event for order ID: {}, Event type: {}", event.getOrderId(), event.getEventType());
        Order order = getOrder(event.getOrderId());

        OrderStatus newStatus = switch (event.getEventType()) {
            case PREPARING -> OrderStatus.PREPARING_FOR_SHIPMENT;
            case SHIPPING -> OrderStatus.SHIPPING;
            case DELIVERED -> OrderStatus.DELIVERED;
        };

        updateOrderStatus(order, newStatus);
        log.info("Updated order status: {}, Order ID: {}", newStatus, order.getId());
    }

    @Transactional
    public Order createOrder(Long userId, OrderRequest request) {
        log.info("Creating new order for user ID: {}, Customer: {}", userId, request.name());

        Order order = orderRequestMapper.orderRequestToOrder(request);
        order.setUserId(userId);
        order.setStatus(OrderStatus.CREATED);

        boolean allReserved = order.getOrderItems().stream()
                .allMatch(item -> productService.reserveInventory(item.getProductId(), item.getQuantity()));

        Order savedOrder = orderRepository.save(order);
        log.info("Order created with ID: {}", savedOrder.getId());

        return savedOrder;
    }

    @Transactional
    public void updateOrderStatus(Order order, OrderStatus status) {
        log.info("Updating order status. Order ID: {}, New status: {}", order.getId(), status);
        order.setStatus(status);
        orderRepository.save(order);
    }

    public Order getOrder(Long orderId) {
        log.debug("Fetching order with ID: {}", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", orderId);
                    return new RuntimeException("주문을 찾을 수 없습니다: " + orderId);
                });
    }

    public OrderResponse getOrderResponse(Long orderId) {
        log.debug("Fetching order response for order ID: {}", orderId);
        Order order = getOrder(orderId);
        return orderMapper.orderToOrderResponse(order);
    }

    public Page<OrderResponse> getOrderList(Pageable pageable) {
        log.debug("Fetching order list. Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(orderMapper::orderToOrderResponse);
    }

    public Page<OrderResponse> getUserOrders(Long userId, Pageable pageable) {
        log.debug("Fetching orders for user ID: {}. Page: {}, Size: {}", userId, pageable.getPageNumber(), pageable.getPageSize());
        Page<Order> ordersWithItems = orderRepository.findByUserId(userId, pageable);
        return ordersWithItems.map(orderMapper::orderToOrderResponse);
    }

    @Transactional
    public void completeOrder(Order order) {
        log.info("Completing order with ID: {}", order.getId());
        updateOrderStatus(order, OrderStatus.ORDER_COMPLETED);
    }

    @Transactional
    public void cancelOrderByOrderId(Long orderId) {
        log.info("Cancelling order with ID: {}", orderId);
        Order order = getOrder(orderId);
        cancelOrder(order);
        paymentService.refundPayment(order);
    }

    @Transactional
    public void cancelOrder(Order order) {
        log.info("Setting order status to CANCELED. Order ID: {}", order.getId());
        updateOrderStatus(order, OrderStatus.CANCELED);
    }

    @Transactional
    public void requestRefund(Long orderId) {
        log.info("Requesting refund for order ID: {}", orderId);
        Order order = getOrder(orderId);
        updateOrderStatus(order, OrderStatus.REFUND_REQUESTED);
    }

    @Scheduled(fixedDelay = 86400000) // 매일 실행
    public void autoConfirmOrders() {
        log.info("Starting auto-confirmation of orders");
        List<Order> ordersToConfirm = getOrdersToConfirm();
        log.info("Found {} orders to auto-confirm", ordersToConfirm.size());

        for (Order order : ordersToConfirm) {
            try {
                confirmOrder(order);
            } catch (Exception e) {
                log.error("Error during auto-confirmation of order ID: {}", order.getId(), e);
            }
        }
    }

    public List<Order> getOrdersToConfirm() {
        LocalDateTime confirmationDate = LocalDateTime.now().minusDays(1);
        log.debug("Fetching orders to confirm before: {}", confirmationDate);
        return orderRepository.findByStatusAndDeliveredAtBefore(OrderStatus.DELIVERED, confirmationDate);
    }

    @Transactional
    public void confirmOrder(Order order) {
        log.info("Confirming order with ID: {}", order.getId());
        updateOrderStatus(order, OrderStatus.ORDER_CONFIRMED);
    }

    @Scheduled(fixedDelay = 86400000) // 매일 실행
    public void autoRefundOrders() {
        log.info("Starting auto-refund of orders");
        List<Order> ordersToRefund = getOrdersToRefund();
        log.info("Found {} orders to auto-refund", ordersToRefund.size());

        for (Order order : ordersToRefund) {
            try {
                refundOrder(order);
            } catch (Exception e) {
                log.error("Error during auto-refund of order ID: {}", order.getId(), e);
            }
        }
    }

    public List<Order> getOrdersToRefund() {
        LocalDateTime refundDate = LocalDateTime.now().minusDays(1);
        log.debug("Fetching orders to refund before: {}", refundDate);
        return orderRepository.findByStatusAndDeliveredAtBefore(OrderStatus.REFUND_REQUESTED, refundDate);
    }

    @Transactional
    public void refundOrder(Order order) {
        log.info("Processing refund for order ID: {}", order.getId());
        updateOrderStatus(order, OrderStatus.REFUND_COMPLETED);
        paymentService.refundPayment(order);
    }

    public boolean isOrderOwnedByUser(Long orderId, Long userId) {
        log.debug("Checking if order ID: {} is owned by user ID: {}", orderId, userId);
        Order order = getOrder(orderId);
        return order.getUserId().equals(userId);
    }
}