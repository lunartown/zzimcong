package com.zzimcong.order.api.controller;

import com.zzimcong.order.application.dto.OrderCreationRequest;
import com.zzimcong.order.application.dto.OrderPreparationRequest;
import com.zzimcong.order.application.dto.OrderPreparationResponse;
import com.zzimcong.order.application.dto.OrderResponse;
import com.zzimcong.order.application.saga.OrderSaga;
import com.zzimcong.order.application.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "order-service")
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderSaga orderSaga;

    @Autowired
    public OrderController(OrderService orderService, OrderSaga orderSaga) {
        this.orderService = orderService;
        this.orderSaga = orderSaga;
    }

    // 주문창 이동
    @PostMapping("/prepare")
    public ResponseEntity<OrderPreparationResponse> prepareOrder(@RequestHeader("X-Auth-User-ID") Long userId,
                                                                 @RequestBody OrderPreparationRequest request) {
        OrderPreparationResponse response = orderSaga.prepareOrder(userId, request);
        return ResponseEntity.ok(response);
    }

    // 사용자 정보 입력 완료, 결제 호출
    @PostMapping("/create/{uuid}")
    public ResponseEntity<Void> createOrder(@RequestHeader("X-Auth-User-ID") Long userId,
                                            @PathVariable String uuid,
                                            @RequestBody OrderCreationRequest request) {
        log.info("Confirming order ID: {} for user ID: {}", uuid, userId);
        orderSaga.createOrder(userId, uuid, request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@RequestHeader("X-Auth-User-ID") Long userId,
                                            @PathVariable Long orderId) {
        orderService.cancelOrderByOrderId(orderId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{orderId}/refund")
    public ResponseEntity<Void> requestRefund(@PathVariable Long orderId) {
        orderService.requestRefund(orderId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@RequestHeader("X-Auth-User-ID") Long userId,
                                                  @PathVariable Long orderId) {
        OrderResponse orderResponse = orderService.getOrderResponse(orderId);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrderList(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OrderResponse> responsePages = orderService.getOrderList(pageRequest);
        return ResponseEntity.ok(responsePages);
    }

    @GetMapping("/user")
    public ResponseEntity<Page<OrderResponse>> getUserOrders(@RequestHeader("X-Auth-User-ID") Long userId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OrderResponse> orderPage = orderService.getUserOrders(userId, pageRequest);
        return ResponseEntity.ok(orderPage);
    }
}