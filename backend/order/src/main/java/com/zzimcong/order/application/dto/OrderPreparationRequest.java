package com.zzimcong.order.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderPreparationRequest(
        BigDecimal orderAmount,
        BigDecimal paymentAmount,
        List<OrderItemRequest> items
) {
}