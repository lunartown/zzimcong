package com.zzimcong.zzimconginventorycore.common.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zzimcong.zzimconginventorycore.common.model.KafkaMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@JsonDeserialize
public class OrderEvent implements KafkaMessage {
    private Long orderId;
    private OrderEventType eventType;
    private String errorMessage;

    public OrderEvent(Long orderId, OrderEventType eventType) {
        this.orderId = orderId;
        this.eventType = eventType;
    }
}