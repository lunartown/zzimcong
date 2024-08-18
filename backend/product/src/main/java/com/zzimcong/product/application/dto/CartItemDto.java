package com.zzimcong.product.application.dto;

import com.zzimcong.product.domain.entity.CartItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemDto {
    private ProductDto productDto;
    private int count;

    //생성자
    public CartItemDto(CartItem cartItem) {
        this.productDto = ProductDto.of(cartItem.getProduct());
        this.count = cartItem.getCount();
    }

    //factory method
    public static CartItemDto of(CartItem cartItem) {
        return new CartItemDto(cartItem);
    }
}
