package com.lunartown.zzimcong.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cart_product")
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_product_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Min(0)
    @Column(nullable = false)
    private int count;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public CartProduct(Long userId, Product product, int count) {
        this.userId = userId;
        this.product = product;
        this.count = count;
    }
}
