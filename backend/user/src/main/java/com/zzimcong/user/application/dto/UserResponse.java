package com.zzimcong.user.application.dto;

public record UserResponse(
        Long id,
        String email,
        String username,
        String phone
) {
}