package com.zzimcong.order.api.controller;


import com.zzimcong.order.application.dto.*;
import com.zzimcong.order.application.service.OrderService;
import com.zzimcong.order.common.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(UserInfo userInfo,
                                                     @RequestBody OrderRequest orderRequest) {
        OrderResponse order = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<OrderListResponse> getOrderList(UserInfo userInfo,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        Page<OrderResponse> orderPage = orderService.getOrderList(PageRequest.of(page, size));
        OrderListResponse response = new OrderListResponse();
        response.setOrders(orderPage.getContent());
        response.setTotalPages(orderPage.getTotalPages());
        response.setTotalElements(orderPage.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> getOrderDetails(UserInfo userInfo,
                                                         @PathVariable("order-id") Long orderId) {
        OrderResponse order = orderService.getOrderDetails(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{order-id}/items")
    public ResponseEntity<List<OrderItemResponse>> getOrderItems(UserInfo userInfo,
                                                                 @PathVariable("order-id") Long orderId) {
        List<OrderItemResponse> items = orderService.getOrderItems(orderId);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{order-id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(UserInfo userInfo,
                                                           @PathVariable("order-id") Long orderId,
                                                           @RequestBody OrderStatusUpdateRequest request) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(orderId, request.getStatus());
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/{order-id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(UserInfo userInfo,
                                                     @PathVariable("order-id") Long orderId) {
        OrderResponse canceledOrder = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(canceledOrder);
    }

    @PostMapping("/{order-id}/refund")
    public ResponseEntity<OrderResponse> refundOrder(UserInfo userInfo,
                                                     @PathVariable("order-id") Long orderId) {
        try {
            OrderResponse refundedOrder = orderService.refundOrder(orderId);
            return ResponseEntity.ok(refundedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}