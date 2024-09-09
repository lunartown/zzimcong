package com.zzimcong.product.application.dto;

import com.zzimcong.product.domain.entity.Product;
import lombok.Data;

@Data
public class ProductResponseDto {
    private Long productId;
    private Long categoryId;
    private String name;
    private int price;
    private int sale;
    private String content;
    private String image;
    private int stock;
    private String createdAt;
    private boolean deleted;

    public ProductResponseDto(Product product) {
        this.productId = product.getId();
        this.categoryId = product.getCategory().getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.sale = product.getSale();
        this.content = product.getContent();
        this.image = product.getImage();
        this.stock = product.getStock();
        this.createdAt = product.getCreatedAt().toString();
        this.deleted = product.isDeleted();
    }

    public static ProductResponseDto of(Product product) {
        return new ProductResponseDto(product);
    }
}
