package com.zzimcong.zzimconginventorycore.common.event;

public enum ShippingEventType {
    PREPARING("배송 준비 중"),
    SHIPPING("배송 중"),
    DELIVERED("배송 완료"),
    PICKUP_COMPLETED("회수 완료");

    private String description;

    ShippingEventType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}