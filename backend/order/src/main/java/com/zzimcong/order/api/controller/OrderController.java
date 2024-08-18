package com.zzimcong.order.api.controller;

import com.zzimcong.order.application.dto.OrderItemResponse;
import com.zzimcong.order.application.dto.OrderRequest;
import com.zzimcong.order.application.dto.OrderResponse;
import com.zzimcong.order.application.saga.OrderSaga;
import com.zzimcong.order.application.service.OrderService;
import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderSaga orderSaga;

    @Autowired
    public OrderController(OrderService orderService, OrderSaga orderSaga) {
        this.orderService = orderService;
        this.orderSaga = orderSaga;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequest orderRequest) {
        orderSaga.startOrderSaga(orderRequest);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok(OrderResponse.createOrderResponse(order));
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrderList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Order> orderPage = orderService.getOrderList(pageRequest);
        Page<OrderResponse> responsePages = orderPage.map(OrderResponse::createOrderResponse);
        return ResponseEntity.ok(responsePages);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<OrderResponse>> getUserOrders(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Order> orderPage = orderService.getUserOrders(userId, pageRequest);
        Page<OrderResponse> responsePages = orderPage.map(OrderResponse::createOrderResponse);
        return ResponseEntity.ok(responsePages);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrderByOrderId(orderId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{orderId}/refund")
    public ResponseEntity<Void> requestRefund(@PathVariable Long orderId) {
        orderService.requestRefund(orderId);
        return ResponseEntity.accepted().build();
    }


    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItemResponse>> getOrderItems(@PathVariable Long orderId) {
        List<OrderItem> items = orderService.getOrderItems(orderId);
        List<OrderItemResponse> itemResponses = items.stream()
                .map(OrderItemResponse::createOrderItemResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(itemResponses);
    }
}