package com.zzimcong.zzimconginventorycore;

import java.util.Objects;

public class InventoryEvent {
    private Long productId;
    private InventoryEventType eventType;
    private int quantity;

    public InventoryEvent() {
    }

    public InventoryEvent(Long productId, InventoryEventType eventType, int quantity) {
        this.productId = productId;
        this.eventType = eventType;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public InventoryEventType getEventType() {
        return eventType;
    }

    public void setEventType(InventoryEventType eventType) {
        this.eventType = eventType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryEvent that = (InventoryEvent) o;
        return quantity == that.quantity &&
                Objects.equals(productId, that.productId) &&
                eventType == that.eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, eventType, quantity);
    }

    @Override
    public String toString() {
        return "com.zzimcong.InventoryEvent{" +
                "productId=" + productId +
                ", eventType=" + eventType +
                ", quantity=" + quantity +
                '}';
    }
}