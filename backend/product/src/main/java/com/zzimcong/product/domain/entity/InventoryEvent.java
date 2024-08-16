package com.zzimcong.product.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class InventoryEvent {
    private Long productId;
    private InventoryEventType eventType;
    private int quantity;
}