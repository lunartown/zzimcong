package com.zzimcong.product.domain.entity;

import com.zzimcong.product.application.dto.ProductRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")  // 복수형 사용
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String name;

    @Min(0)
    @Column(nullable = false)
    private int price;

    @Min(0)
    @Column(nullable = false)
    private int sale;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Size(max = 255)
    private String image;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Product(ProductRequestDto productRequestDto, Category category) {
        this.name = productRequestDto.getName();
        this.price = productRequestDto.getPrice();
        this.sale = productRequestDto.getSale();
        this.content = productRequestDto.getContent();
        this.image = productRequestDto.getImage();
        this.stock = productRequestDto.getStock();
        this.category = category;
    }
}