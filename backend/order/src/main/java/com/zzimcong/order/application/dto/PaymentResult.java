package com.zzimcong.order.application.dto;

import com.zzimcong.zzimconginventorycore.common.model.KafkaMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResult implements KafkaMessage {
    private Long orderId;
    private boolean success;
    private String status;

    public PaymentResult(Long orderId, boolean success) {
        this.orderId = orderId;
        this.success = success;
        this.status = success ? "PAID" : "FAILED";
    }

    public PaymentResult(Long orderId, boolean success, String status) {
        this.orderId = orderId;
        this.success = success;
        this.status = status;
    }
}