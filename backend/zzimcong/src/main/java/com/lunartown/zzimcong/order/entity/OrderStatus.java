package com.lunartown.zzimcong.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    RESERVED("재고 확보"),
    ORDER_COMPLETED("주문 완료"),
    PREPARING_DELIVERY("배송 준비중"),
    SHIPPING("배송 중"),
    DELIVERED("배송 완료"),
    ORDER_CONFIRMED("주문 확정"),
    CANCELED("취소됨"),
    REFUNDED("환불됨");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

}