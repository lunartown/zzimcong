package com.zzimcong.order.application.dto;

import com.zzimcong.zzimconginventorycore.common.model.KafkaMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentResponse implements KafkaMessage {
    private Long userId;
    private String uuid;
    private boolean success;
    private String status;

    public PaymentResponse(Long userId, String uuid, boolean success) {
        this.userId = userId;
        this.uuid = uuid;
        this.success = success;
        this.status = success ? "PAID" : "FAILED";
    }

    public PaymentResponse(Long userId, String uuid, boolean success, String status) {
        this.userId = userId;
        this.uuid = uuid;
        this.success = success;
        this.status = status;
    }
}