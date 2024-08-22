package com.zzimcong.order.application.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderPreparationStatus {
    SUCCESS("주문 성공"),
    INVENTORY_SHORTAGE("재고 부족"),
    FAILED("주문 실패");


    private final String message;
}