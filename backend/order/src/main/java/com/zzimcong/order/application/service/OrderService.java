package com.zzimcong.order.application.service;

import com.zzimcong.order.application.dto.OrderResponse;
import com.zzimcong.order.application.dto.RefundResponse;
import com.zzimcong.order.application.mapper.OrderMapper;
import com.zzimcong.order.common.exception.ErrorCode;
import com.zzimcong.order.common.exception.NotFoundException;
import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderStatus;
import com.zzimcong.order.domain.repository.OrderRepository;
import com.zzimcong.zzimconginventorycore.common.event.ShippingEvent;
import com.zzimcong.zzimconginventorycore.common.event.ShippingEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final OrderMapper orderMapper;
    private final KafkaTemplate<String, Order> kafkaTemplate;

    /**********************************************
     배송 준비 중, 배송 중, 배송완료, 회수완료 처리
     **********************************************/
    @Transactional
    @KafkaListener(topics = "shipping-events")
    public void handleShippingEvent(ShippingEvent event) {
        log.info("Received shipping event for order ID: {}, Event type: {}", event.getOrderId(), event.getEventType());
        Order order = getOrder(event.getOrderId());

        if (event.getEventType() == ShippingEventType.PICKUP_COMPLETED) {
            // PICKUP_COMPLETED 이벤트 별도 처리
            refundOrder(order);
            log.info("Processed PICKUP_COMPLETED event for Order ID: {}", order.getId());
        } else {
            // 다른 이벤트들에 대한 상태 업데이트
            updateOrderStatusForShippingEvent(order, event.getEventType());
        }
    }

    // 배송 이벤트에 따른 주문 상태 업데이트
    private void updateOrderStatusForShippingEvent(Order order, ShippingEventType eventType) {
        OrderStatus newStatus = switch (eventType) {
            case PREPARING -> OrderStatus.PREPARING_FOR_SHIPMENT;
            case SHIPPING -> OrderStatus.SHIPPING;
            case DELIVERED -> OrderStatus.DELIVERED;
            default -> throw new IllegalArgumentException("예상치 못한 배송 이벤트 타입: " + eventType);
        };
        updateOrderStatus(order, newStatus);
        log.info("Updated order status: {}, Order ID: {}", newStatus, order.getId());
    }

    // 주문 상태 업데이트
    @Transactional
    public void updateOrderStatus(Order order, OrderStatus status) {
        log.info("Updating order status. Order ID: {}, New status: {}", order.getId(), status);
        order.setStatus(status);
        orderRepository.save(order);
    }


    /**********************************************
     취소, 환불 처리
     **********************************************/
    // 주문 취소 요청
    @Transactional
    public void cancelOrderByOrderId(Long orderId) {
        log.info("Cancelling order with ID: {}", orderId);
        Order order = getOrder(orderId);
        tryCancelOrder(order);
        kafkaTemplate.send("refund-request", order);
    }

    // 주문 취소 가능 여부 확인
    @Transactional
    public void tryCancelOrder(Order order) {
        log.info("Setting order status to CANCELED. Order ID: {}", order.getId());
        if (order.getStatus() == OrderStatus.PREPARING_FOR_SHIPMENT) {
            throw new RuntimeException("배송 준비 중인 주문은 취소할 수 없습니다: " + order.getId());
        } else if (order.getStatus() == OrderStatus.SHIPPING) {
            throw new RuntimeException("배송 중인 주문은 취소할 수 없습니다: " + order.getId());
        } else if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("배송 완료된 주문은 취소할 수 없습니다: " + order.getId());
        } else if (order.getStatus() != OrderStatus.ORDER_COMPLETED) {
            throw new RuntimeException("취소할 수 없는 주문 상태입니다: " + order.getId());
        }
    }

    // 환불 요청 수신
    @Transactional
    public void requestRefund(Long orderId) {
        log.info("Requesting refund for order ID: {}", orderId);
        Order order = getOrder(orderId);
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new RuntimeException("배송 완료된 주문만 환불할 수 있습니다: " + order.getId());
        } else {
            updateOrderStatus(order, OrderStatus.REFUND_REQUESTED);
        }
    }

    // 환불 완료 처리 요청
    @Transactional
    public void refundOrder(Order order) {
        log.info("Processing refund for order ID: {}", order.getId());
        kafkaTemplate.send("refund-request", order);
    }

    // 환불 결과 처리
    @Transactional
    @KafkaListener(topics = "refund-results")
    public void handleRefundResult(RefundResponse response) {
        log.info("Received refund result: {}", response);
        if (response.isSuccess()) {
            if (response.getStatus() == OrderStatus.REFUND_REQUESTED) {
                updateOrderStatus(getOrder(response.getOrderId()), OrderStatus.REFUND_COMPLETED);
            } else {
                updateOrderStatus(getOrder(response.getOrderId()), OrderStatus.CANCELED);
            }
        }
    }


    /**********************************************
     주문 조회
     **********************************************/
    public Order getOrder(Long orderId) {
        log.debug("Fetching order with ID: {}", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", orderId);
                    return new NotFoundException(ErrorCode.ORDER_NOT_FOUND);
                });
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderResponse(Long orderId) {
        log.debug("Fetching order response for order ID: {}", orderId);
        Order order = getOrder(orderId);
        return orderMapper.orderToOrderResponse(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrderList(Pageable pageable) {
        log.debug("Fetching order list. Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(orderMapper::orderToOrderResponse);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getUserOrders(Long userId, Pageable pageable) {
        log.debug("Fetching orders for user ID: {}. Page: {}, Size: {}", userId, pageable.getPageNumber(), pageable.getPageSize());
        Page<Order> ordersWithItems = orderRepository.findByUserId(userId, pageable);
        return ordersWithItems.map(orderMapper::orderToOrderResponse);
    }


    /**********************************************
     주문 자동 확정
     **********************************************/
    // 매일 배송완료 주문 자동 확정
    @Scheduled(fixedDelay = 86400000)
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


    /**********************************************
     주문 소유자 확인
     **********************************************/
    public boolean isOrderOwnedByUser(Long orderId, Long userId) {
        log.debug("Checking if order ID: {} is owned by user ID: {}", orderId, userId);
        Order order = getOrder(orderId);
        return order.getUserId().equals(userId);
    }
}