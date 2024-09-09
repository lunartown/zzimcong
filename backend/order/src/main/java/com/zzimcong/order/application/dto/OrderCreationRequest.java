package com.zzimcong.order.application.dto;

public record OrderCreationRequest(
        OrderAddressRequest orderAddressRequest,
        PaymentDetailsRequest paymentDetailsRequest
) {
}