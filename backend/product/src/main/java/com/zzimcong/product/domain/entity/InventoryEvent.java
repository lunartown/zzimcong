package com.zzimcong.product.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InventoryEvent {
    private Long productId;
    private InventoryEventType eventType;
    private int quantity;
}