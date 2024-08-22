package com.zzimcong.order.application.dto;

import com.zzimcong.order.domain.entity.PaymentType;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        BigDecimal orderAmount,
        BigDecimal paymentAmount,
        PaymentType payment,
        String name,
        String addr,
        String addrDetail,
        String zipcode,
        String phone,
        String message,
        List<OrderItemRequest> items
) {
}
