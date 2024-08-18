package com.zzimcong.order.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemRequest {
    public Long productId;
    public BigDecimal price;
    public Integer quantity;
}
