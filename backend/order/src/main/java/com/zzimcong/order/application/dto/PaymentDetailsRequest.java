package com.zzimcong.order.application.dto;

import com.zzimcong.order.domain.entity.PaymentType;

public record PaymentDetailsRequest(
        PaymentType paymentMethod,
        String cardNumber,
        String cardHolderName,
        String expirationDate,
        String cvv
) {
}