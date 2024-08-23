package com.zzimcong.order.application.dto;

public record OrderAddressRequest(
        String name,
        String streetAddress,
        String addressDetail,
        String zipcode,
        String phone,
        String message) {
}
