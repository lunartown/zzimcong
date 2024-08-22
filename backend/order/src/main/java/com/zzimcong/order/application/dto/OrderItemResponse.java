package com.zzimcong.order.application.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long productId,
        BigDecimal price,
        Integer quantity
) {
}