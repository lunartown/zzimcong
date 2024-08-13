package com.lunartown.zzimcong.order.service;

import com.lunartown.zzimcong.order.dto.OrderItemResponse;
import com.lunartown.zzimcong.order.dto.OrderRequest;
import com.lunartown.zzimcong.order.dto.OrderResponse;
import com.lunartown.zzimcong.order.entity.Order;
import com.lunartown.zzimcong.order.entity.OrderItem;
import com.lunartown.zzimcong.order.entity.OrderStatus;
import com.lunartown.zzimcong.order.exception.OrderNotFoundException;
import com.lunartown.zzimcong.order.repository.OrderItemRepository;
import com.lunartown.zzimcong.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "order-service")
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest orderRequest) {
        Order order = createAndSaveOrder(userId, orderRequest);
        List<OrderItem> orderItems = createAndSaveOrderItems(order, orderRequest);
        return OrderResponse.createOrderResponse(order, orderItems);
    }

    private Order createAndSaveOrder(Long userId, OrderRequest orderRequest) {
        Order order = Order.createOrder(userId, orderRequest);

        orderRequest.getItems().stream()
                .map(item -> OrderItem.createOrderItem(order, item))
                .forEach(order::addOrderItem);

        return orderRepository.save(order);
    }

    private List<OrderItem> createAndSaveOrderItems(Order order, OrderRequest orderRequest) {
        List<OrderItem> orderItems = orderRequest.getItems().stream()
                .map(item -> OrderItem.createOrderItem(order, item))
                .collect(Collectors.toList());
        return orderItemRepository.saveAll(orderItems);
    }

    public OrderResponse getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다: " + orderId));
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        return OrderResponse.createOrderResponse(order, orderItems);
    }

    // 주문이 사용자에 의해 소유되었는지 확인하는 메서드
    public boolean isOrderOwnedByUser(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId).isPresent();
    }

    public List<OrderItemResponse> getOrderItems(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        return orderItems.stream()
                .map(OrderItemResponse::createOrderItemResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        log.info("Updating order status. OrderId: {}, New status: {}", orderId, status);

        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다: " + orderId));

        // 상태 유효성 검증
        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid order status provided: {}", status);
            throw new IllegalArgumentException("유효하지 않은 주문 상태입니다: " + status);
        }

        // 상태 업데이트
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        log.info("Order status updated successfully. OrderId: {}, New status: {}", orderId, newStatus);

        // 주문 아이템 조회 및 응답 생성
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        return OrderResponse.createOrderResponse(updatedOrder, orderItems);
    }

    public OrderResponse cancelOrder(Long orderId) {
        log.info("Canceling order. OrderId: {}", orderId);

        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다: " + orderId));

        if (!order.getStatus().equals(OrderStatus.ORDER_COMPLETED)) {
            throw new IllegalArgumentException("취소할 수 없는 주문 상태입니다: " + order.getStatus());
        } else {
            order.setStatus(OrderStatus.CANCELED);
            Order canceledOrder = orderRepository.save(order);
            log.info("Order canceled successfully. OrderId: {}", orderId);

            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
            return OrderResponse.createOrderResponse(canceledOrder, orderItems);
        }
    }

    public OrderResponse refundOrder(Long orderId) {
        log.info("Refunding order. OrderId: {}", orderId);

        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다: " + orderId));

        if (!order.getStatus().equals(OrderStatus.DELIVERED)) {
            throw new IllegalArgumentException("배송 완료 후 1일 경과하여 환불이 불가합니다: " + order.getStatus());
        } else {
            order.setStatus(OrderStatus.REFUNDED);
            Order canceledOrder = orderRepository.save(order);
            log.info("Order refunded successfully. OrderId: {}", orderId);

            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
            return OrderResponse.createOrderResponse(canceledOrder, orderItems);
        }
    }

    public Page<OrderResponse> getOrderList(Pageable pageable) {
        // OrderRepository에서 Page<Order>를 가져온 후 OrderResponse로 변환
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(OrderResponse::createOrderResponse);
    }
}
