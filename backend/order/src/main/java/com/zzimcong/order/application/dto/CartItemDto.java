package com.zzimcong.order.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemDto {
    public Long productId;
    public Integer price;
    public Integer quantity;
}
