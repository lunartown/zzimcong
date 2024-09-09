package com.zzimcong.zzimconginventorycore.common.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zzimcong.zzimconginventorycore.common.model.KafkaMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@JsonDeserialize
public class ShippingEvent implements KafkaMessage {
    private Long orderId;
    private ShippingEventType eventType;
}