package com.zzimcong.user.application.dto;

public record LoginRequest(
        String email,
        String password
) {
}