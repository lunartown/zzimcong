package com.zzimcong.order.application.dto;

import com.zzimcong.order.domain.entity.OrderStatus;
import com.zzimcong.zzimconginventorycore.common.model.KafkaMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RefundResponse implements KafkaMessage {
    private Long userId;
    private Long orderId;
    private boolean success;
    private OrderStatus status;

    public RefundResponse(Long userId, Long orderId, boolean success, OrderStatus status) {
        this.userId = userId;
        this.orderId = orderId;
        this.success = success;
        this.status = status;
    }
}