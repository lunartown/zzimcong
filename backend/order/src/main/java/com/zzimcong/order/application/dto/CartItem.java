package com.zzimcong.order.application.dto;

public record CartItem(
        Long productId,
        Integer price,
        Integer quantity
) {
}
