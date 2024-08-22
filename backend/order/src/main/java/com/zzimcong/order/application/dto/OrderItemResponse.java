package com.zzimcong.order.application.dto;

import com.zzimcong.order.domain.entity.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long productId,
        BigDecimal price,
        Integer quantity
) {
    public static OrderItemResponse createOrderItemResponse(OrderItem orderItem) {
        return new OrderItemResponse(orderItem.getId(), orderItem.getProductId(), orderItem.getPrice(), orderItem.getQuantity());
    }
}