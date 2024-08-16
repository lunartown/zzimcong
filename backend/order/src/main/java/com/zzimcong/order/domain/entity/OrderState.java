package com.zzimcong.order.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderState {
    CREATED("주문 생성됨"),
    STOCK_RESERVED("재고 확보 완료"),
    PAYMENT_PROCESSED("결제 완료"),
    ORDER_COMPLETED("주문 완료"),
    PREPARING_FOR_SHIPMENT("배송 준비 중"),
    SHIPPING("배송 중"),
    DELIVERED("배송 완료"),
    ORDER_CONFIRMED("주문 확정"),
    CANCELED("취소됨"),
    REFUND_REQUESTED("환불 신청됨"),
    REFUND_COMPLETED("환불 완료됨");

    private final String description;
}