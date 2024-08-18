package com.zzimcong.order.infrastructure.kafka;

import com.zzimcong.order.domain.entity.PaymentType;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Setter
public class PaymentEvent {
    private Long orderId;
    private BigDecimal amount;
    private PaymentType payment;
    private boolean success;

    public PaymentEvent(Long orderId, BigDecimal amount, PaymentType payment) {
        this.orderId = orderId;
        this.amount = amount;
        this.payment = payment;
        this.success = false;
    }
}