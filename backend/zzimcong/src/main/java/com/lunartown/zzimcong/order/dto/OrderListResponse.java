package com.lunartown.zzimcong.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderListResponse {
    private List<OrderResponse> orders;
    private int totalPages;
    private long totalElements;
}
