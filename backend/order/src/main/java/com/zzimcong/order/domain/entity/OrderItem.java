package com.zzimcong.order.domain.entity;

import com.zzimcong.order.application.dto.OrderItemRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    public Long id;

    @Column(nullable = false)
    public Long productId;

    @Column
    public Integer price;

    @Column(nullable = false)
    public Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    public Order order;

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
