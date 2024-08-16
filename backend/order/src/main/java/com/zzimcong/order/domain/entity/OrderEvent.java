package com.zzimcong.order.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderEvent {
    private Long orderId;
    private OrderEventType orderEventType;
}
