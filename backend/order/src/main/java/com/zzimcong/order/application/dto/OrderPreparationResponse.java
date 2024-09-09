package com.zzimcong.order.application.dto;

public record OrderPreparationResponse(
        String orderId,
        OrderPreparationStatus status
) {
}
