package com.zzimcong.order.application.dto;

import java.math.BigDecimal;

public record OrderItemRequest(
        Long productId,
        BigDecimal price,
        Integer quantity
) {
}
