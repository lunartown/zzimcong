package com.zzimcong.order.application.dto;

import java.util.List;

public record OrderListResponse(
        List<OrderResponse> orders,
        int totalPages,
        long totalElements
) {
}
