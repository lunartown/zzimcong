package com.zzimcong.user.application.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}