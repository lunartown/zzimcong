package com.zzimcong.order.application.dto;

import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemResponse {
    public Long id;
    public Long productId;
    public BigDecimal price;
    public Integer quantity;
    public Order order;

    private OrderItemResponse(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.productId = orderItem.getProductId();
        this.price = orderItem.getPrice();
        this.quantity = orderItem.getQuantity();
        this.order = orderItem.getOrder();
    }

    //factory method
    public static OrderItemResponse createOrderItemResponse(OrderItem orderItem) {
        return new OrderItemResponse(orderItem);
    }
}
