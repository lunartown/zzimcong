package com.zzimcong.zzimconginventorycore.common.event;

import com.zzimcong.zzimconginventorycore.common.model.KafkaMessage;

public class OrderEvent implements KafkaMessage {
    private Long orderId;
    private OrderEventType eventType;
    private String errorMessage;

    public OrderEvent(Long orderId, OrderEventType eventType) {
        this.orderId = orderId;
        this.eventType = eventType;
    }

    public OrderEvent(Long orderId, OrderEventType eventType, String errorMessage) {
        this.orderId = orderId;
        this.eventType = eventType;
        this.errorMessage = errorMessage;
    }

    public Long getOrderId() {
        return orderId;
    }

    public OrderEventType getEventType() {
        return eventType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setEventType(OrderEventType eventType) {
        this.eventType = eventType;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}