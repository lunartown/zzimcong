package com.zzimcong.order.domain.entity;

import com.zzimcong.order.application.dto.OrderItemRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_items") // 복수형 사용
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private OrderItem(Order order, OrderItemRequest orderItemRequest) {
        this.productId = orderItemRequest.getProductId();
        this.price = orderItemRequest.getPrice();
        this.quantity = orderItemRequest.getQuantity();
        this.order = order;
    }

    //factory method
    public static OrderItem createOrderItem(Order order, OrderItemRequest orderItemRequest) {
        return new OrderItem(order, orderItemRequest);
    }
}