package com.lunartown.zzimcong.order.controller;


import com.lunartown.zzimcong.order.dto.OrderItemResponse;
import com.lunartown.zzimcong.order.dto.OrderListResponse;
import com.lunartown.zzimcong.order.dto.OrderResponse;
import com.lunartown.zzimcong.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<OrderResponse> createOrder(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @RequestBody OrderRequest orderRequest) {
        OrderResponse order = orderService.createOrder(userPrincipal.getId(), orderRequest);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<OrderListResponse> getOrderList(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        Page<OrderResponse> orderPage = orderService.getOrderList(userPrincipal.getId(), PageRequest.of(page, size));
        OrderListResponse response = new OrderListResponse();
        response.setOrders(orderPage.getContent());
        response.setTotalPages(orderPage.getTotalPages());
        response.setTotalElements(orderPage.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> getOrderDetails(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                         @PathVariable("order-id") Long orderId) {
        OrderResponse order = orderService.getOrderDetails(userPrincipal.getId(), orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{order-id}/items")
    public ResponseEntity<List<OrderItemResponse>> getOrderItems(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                 @PathVariable("order-id") Long orderId) {
        List<OrderItemResponse> items = orderService.getOrderItems(userPrincipal.getId(), orderId);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{order-id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                           @PathVariable("order-id") Long orderId,
                                                           @RequestBody OrderStatusUpdateRequest request) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(userPrincipal.getId(), orderId, request.getStatus());
        return ResponseEntity.ok(updatedOrder);
    }

    @PostMapping("/{order-id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @PathVariable("order-id") Long orderId) {
        OrderResponse canceledOrder = orderService.cancelOrder(userPrincipal.getId(), orderId);
        return ResponseEntity.ok(canceledOrder);
    }

    @PostMapping("/{order-id}/refund")
    public ResponseEntity<OrderResponse> refundOrder(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @PathVariable("order-id") Long orderId) {
        OrderResponse refundedOrder = orderService.refundOrder(userPrincipal.getId(), orderId);
        return ResponseEntity.ok(refundedOrder);
    }
}