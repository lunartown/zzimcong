package com.zzimcong.order.application.dto;

import com.zzimcong.zzimconginventorycore.common.model.KafkaMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest implements KafkaMessage {
    private Long userId;
    private String uuid;
    private PaymentDetailsRequest paymentDetails;
    private BigDecimal amount;
}