package com.lunartown.zzimcong.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddToCartRequestDto {
    private Long productId;
    private int count;
}