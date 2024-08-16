package com.zzimcong.order.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    public Long productId;
    public Integer price;
    public Integer quantity;
}
