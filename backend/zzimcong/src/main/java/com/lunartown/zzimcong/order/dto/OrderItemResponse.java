package com.lunartown.zzimcong.order.dto;

import com.lunartown.zzimcong.order.entity.Order;
import com.lunartown.zzimcong.order.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
    public Long id;
    public Long productId;
    public Integer price;
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
