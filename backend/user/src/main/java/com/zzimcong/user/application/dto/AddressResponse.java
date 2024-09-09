package com.zzimcong.user.application.dto;

public record AddressResponse(
        Long id,
        String name,
        String streetAddress,
        String addressDetail,
        String zipcode,
        String phone,
        String message,
        boolean isDefault
) {
}
