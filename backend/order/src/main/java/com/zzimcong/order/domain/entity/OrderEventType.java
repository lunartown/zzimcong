package com.zzimcong.order.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderEventType {
    RESERVE_STOCK("재고 확보"),
    PROCESS_PAYMENT("결제 처리"),
    COMPLETE_ORDER("주문 완료"),
    PREPARE_SHIPMENT("배송 준비"),
    SHIP_ORDER("배송"),
    DELIVER_ORDER("배송 완료"),
    CONFIRM_ORDER("주문 확정"),
    CANCEL_ORDER("주문 취소"),
    REQUEST_REFUND("환불 요청"),
    COMPLETE_REFUND("환불 완료");

    private final String description;
}