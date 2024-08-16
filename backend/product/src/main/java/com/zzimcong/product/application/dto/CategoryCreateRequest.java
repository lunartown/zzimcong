package com.zzimcong.product.application.dto;

import lombok.Data;

@Data
public class CategoryCreateRequest {
    private String name;
    private Long parentCategoryId;
}
