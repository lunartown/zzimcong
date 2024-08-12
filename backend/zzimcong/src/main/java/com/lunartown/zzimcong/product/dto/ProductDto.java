package com.lunartown.zzimcong.product.dto;

import com.lunartown.zzimcong.product.entity.Product;
import lombok.Data;

@Data
public class ProductDto {
    private Long productId;
    private Long categoryId;
    private String name;
    private int price;
    private int sale;
    private String content;
    private String image;
    private int count;
    private String createdAt;
    private boolean deleted;

    public ProductDto(Product product) {
        this.productId = product.getId();
        this.categoryId = product.getCategory().getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.sale = product.getSale();
        this.content = product.getContent();
        this.image = product.getImage();
        this.count = product.getCount();
        this.createdAt = product.getCreatedAt().toString();
        this.deleted = product.isDeleted();
    }

    public static ProductDto of(Product product) {
        return new ProductDto(product);
    }
}
