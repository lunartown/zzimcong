package com.zzimcong.zzimconginventorycore.common.event;

public class ShippingEvent {
    private Long orderId;
    private ShippingEventType eventType;

    public ShippingEvent(Long orderId, ShippingEventType eventType) {
        this.orderId = orderId;
        this.eventType = eventType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public ShippingEventType getEventType() {
        return eventType;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setEventType(ShippingEventType eventType) {
        this.eventType = eventType;
    }
}