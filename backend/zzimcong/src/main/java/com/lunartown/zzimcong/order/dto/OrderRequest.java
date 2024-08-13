package com.lunartown.zzimcong.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    public Long orderAmount;
    public Long paymentAmount;
    public String payment;
    public String name;
    public String addr;
    public String addrDetail;
    public String zipcode;
    public String phone;
    public String message;
    public List<OrderItemRequest> items;
}
