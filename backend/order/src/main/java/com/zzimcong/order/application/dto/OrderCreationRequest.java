package com.zzimcong.order.application.dto;

import com.zzimcong.order.domain.entity.OrderAddressRequest;

public record OrderCreationRequest(
        OrderAddressRequest orderAddressRequest,
        PaymentDetailsRequest paymentDetailsRequest
) {
}