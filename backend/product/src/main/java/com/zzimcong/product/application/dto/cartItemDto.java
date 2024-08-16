package com.zzimcong.product.application.dto;

import com.zzimcong.product.domain.entity.CartItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class cartItemDto {
    private ProductDto productDto;
    private int count;

    //생성자
    public cartItemDto(CartItem cartItem) {
        this.productDto = ProductDto.of(cartItem.getProduct());
        this.count = cartItem.getCount();
    }

    //factory method
    public static cartItemDto of(CartItem cartItem) {
        return new cartItemDto(cartItem);
    }
}
