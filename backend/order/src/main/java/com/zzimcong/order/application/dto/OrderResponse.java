package com.zzimcong.order.application.dto;

import com.zzimcong.order.domain.entity.OrderStatus;
import com.zzimcong.order.domain.entity.PaymentType;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        Long id,
        Long userId,
        BigDecimal orderAmount,
        BigDecimal paymentAmount,
        PaymentType payment,
        OrderStatus status,
        boolean deleted,
        String reason,
        OrderAddressResponse orderAddress,
        List<OrderItemResponse> items
) {
}
