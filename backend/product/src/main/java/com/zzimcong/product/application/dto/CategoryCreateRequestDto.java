package com.zzimcong.product.application.dto;

import lombok.Data;

@Data
public class CategoryCreateRequestDto {
    private String name;
    private Long parentCategoryId;
}
