package com.zzimcong.order.application.dto;

import com.zzimcong.zzimconginventorycore.common.model.KafkaMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class PaymentRequest implements KafkaMessage {
    private Long orderId;
    private BigDecimal amount;
}