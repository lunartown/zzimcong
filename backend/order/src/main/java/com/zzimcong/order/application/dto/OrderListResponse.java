package com.zzimcong.order.application.dto;

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
