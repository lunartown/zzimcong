package com.zzimcong.order.application.dto;

public record PaymentDetailsRequest(
        String paymentMethod,
        String cardNumber,
        String cardHolderName,
        String expirationDate,
        String cvv
) {
}