package com.lunartown.zzimcong.product.dto;

import com.lunartown.zzimcong.product.entity.CartProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartProductDto {
    private ProductDto productDto;
    private int count;

    //생성자
    public CartProductDto(CartProduct cartProduct) {
        this.productDto = ProductDto.of(cartProduct.getProduct());
        this.count = cartProduct.getCount();
    }

    //factory method
    public static CartProductDto of(CartProduct cartProduct) {
        return new CartProductDto(cartProduct);
    }
}
