package com.zzimcong.zzimconginventorycore.common.event;

public enum OrderEventType {
    ORDER_CREATED("주문 생성"),
    INVENTORY_RESERVED("재고 예약"),
    PAYMENT_PROCESSED("결제 완료"),
    SHIPMENT_PREPARING("배송 준비 시작"),
    SHIPMENT_STARTED("배송 출발"),
    SHIPMENT_DELIVERED("배송 도착"),
    ORDER_COMPLETED("주문 완료"),
    SAGA_FAILED("주문 실패");

    private final String description;

    OrderEventType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}