package com.lunartown.zzimcong.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResultDto {
    private String accessToken;
    private String refreshToken;
}