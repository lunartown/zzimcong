package com.zzimcong.order.application.dto;

public record OrderAddressResponse(
        String name,
        String streetAddress,
        String addressDetail,
        String zipcode,
        String phone,
        String message) {
}
