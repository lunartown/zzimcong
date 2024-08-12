package com.lunartown.zzimcong.product.dto;

import lombok.Data;

@Data
public class CategoryCreateRequest {
    private String name;
    private Long parentCategoryId;
}
