package com.zzimcong.user.application.dto;

public record UserModifyRequest(
        String name,
        String phone,
        String password
) {
}
