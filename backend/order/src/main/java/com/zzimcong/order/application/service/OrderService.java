package com.zzimcong.order.application.service;

import com.zzimcong.order.application.dto.OrderItemRequest;
import com.zzimcong.order.application.dto.OrderRequest;
import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderItem;
import com.zzimcong.order.domain.entity.OrderStatus;
import com.zzimcong.order.domain.repository.OrderRepository;
import com.zzimcong.zzimconginventorycore.common.event.ShippingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "order-service")
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    @Autowired
    public OrderService(OrderRepository orderRepository, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "shipping-events")
    @Transactional
    public void handleShippingEvent(ShippingEvent event) {
        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다: " + event.getOrderId()));

        switch (event.getEventType()) {
            case PREPARING:
                order.setStatus(OrderStatus.PREPARING_FOR_SHIPMENT);
                break;
            case SHIPPING:
                order.setStatus(OrderStatus.SHIPPING);
                break;
            case DELIVERED:
                order.setStatus(OrderStatus.DELIVERED);
                break;
        }

        orderRepository.save(order);
        log.info("주문 상태 업데이트: {}, 주문 ID: {}", order.getStatus(), order.getId());
    }

    @Transactional
    public Order createOrder(OrderRequest request) {
        log.info("Creating new order for customer: {}", request.getName());

        // 주문 생성
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
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
        }
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        log.info("Order created with ID: {}", savedOrder.getId());

        return savedOrder;
    }

    @Transactional
    public void updateOrderStatus(Order order, OrderStatus status) {
        order.setStatus(status);
        orderRepository.save(order);
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
    }

    public Page<Order> getOrderList(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Page<Order> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        Order order = getOrder(orderId);
        return order.getOrderItems();
    }

    @Transactional
    public void completeOrder(Order order) {
        order.setStatus(OrderStatus.ORDER_COMPLETED);
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrderByOrderId(Long orderId) {
        Order order = getOrder(orderId);
        cancelOrder(order);
        paymentService.refundPayment(order);
    }

    @Transactional
    public void cancelOrder(Order order) {
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    @Transactional
    public void requestRefund(Long orderId) {
        Order order = getOrder(orderId);
        order.setStatus(OrderStatus.REFUND_REQUESTED);
        orderRepository.save(order);
    }

    @Scheduled(fixedDelay = 86400000) // 매일 실행
    public void autoConfirmOrders() {
        List<Order> ordersToConfirm = getOrdersToConfirm();
        for (Order order : ordersToConfirm) {
            try {
                confirmOrder(order);
            } catch (Exception e) {
                // 로깅 및 예외 처리
            }
        }
    }

    public List<Order> getOrdersToConfirm() {
        LocalDateTime confirmationDate = LocalDateTime.now().minusDays(1);
        return orderRepository.findByStatusAndDeliveredAtBefore(OrderStatus.DELIVERED, confirmationDate);
    }

    @Transactional
    public void confirmOrder(Order order) {
        order.setStatus(OrderStatus.ORDER_CONFIRMED);
        orderRepository.save(order);
    }

    @Scheduled(fixedDelay = 86400000) // 매일 실행
    public void autoRefundOrders() {
        List<Order> ordersToRefund = getOrdersToRefund();
        for (Order order : ordersToRefund) {
            try {
                refundOrder(order);
            } catch (Exception e) {
                // 로깅 및 예외 처리
            }
        }
    }

    public List<Order> getOrdersToRefund() {
        LocalDateTime confirmationDate = LocalDateTime.now().minusDays(1);
        return orderRepository.findByStatusAndDeliveredAtBefore(OrderStatus.REFUND_REQUESTED, confirmationDate);
    }

    @Transactional
    public void refundOrder(Order order) {
        order.setStatus(OrderStatus.REFUND_COMPLETED);
        orderRepository.save(order);
        paymentService.refundPayment(order);
    }

    public boolean isOrderOwnedByUser(Long orderId, Long id) {
        Order order = getOrder(orderId);
        return order.getUserId().equals(id);
    }
}
