package com.zzimcong.product.application.dto;

import lombok.Data;

@Data
public class ProductRequestDto {
    private Long categoryId;
    private String name;
    private int price;
    private int sale;
    private String content;
    private String image;
    private int stock;
}
