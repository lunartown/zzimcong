package com.zzimcong.order.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private BigDecimal orderAmount;
    private BigDecimal paymentAmount;
    private PaymentType payment;
    private String name;
    private String addr;
    private String addrDetail;
    private String zipcode;
    private String phone;
    private String message;
    private List<OrderItemRequest> items;

    public enum PaymentType {
        KB, KAKAO, NAVER, KEB, IBK, NH
    }
}
