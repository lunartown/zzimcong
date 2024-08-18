package com.zzimcong.order.application.dto;

import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderItem;
import com.zzimcong.order.domain.entity.OrderStatus;
import com.zzimcong.order.domain.entity.PaymentType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderResponse {
    public Long id;
    public Long userId;
    public BigDecimal orderAmount;
    public BigDecimal paymentAmount;
    public PaymentType payment;
    public OrderStatus status;
    public boolean deleted = false;
    public String reason;
    public String name;
    public String addr;
    public String addrDetail;
    public String zipcode;
    public String phone;
    public String message;
    public List<OrderItemResponse> items;

    private OrderResponse(Order order) {
        this.setId(order.getId());
        this.setUserId(order.getUserId());
        this.setOrderAmount(order.getOrderAmount());
        this.setPaymentAmount(order.getPaymentAmount());
        this.setPayment(order.getPayment());
        this.setStatus(order.getStatus());
        this.setDeleted(order.isDeleted());
        this.setReason(order.getReason());
        this.setName(order.getName());
        this.setAddr(order.getAddr());
        this.setAddrDetail(order.getAddrDetail());
        this.setZipcode(order.getZipcode());
        this.setPhone(order.getPhone());
        this.setMessage(order.getMessage());
    }

    private OrderResponse(Order order, List<OrderItem> orderItems) {
        this(order);
        this.setItems(orderItems.stream()
                .map(OrderItemResponse::createOrderItemResponse)
                .collect(Collectors.toList()));
    }

    //factory method
    public static OrderResponse createOrderResponse(Order order) {
        return new OrderResponse(order);
    }

    public static OrderResponse createOrderResponse(Order order, List<OrderItem> orderItems) {
        return new OrderResponse(order, orderItems);
    }
}
