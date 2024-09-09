package com.zzimcong.user.application.service;

import com.zzimcong.user.infrastructure.config.RefreshTokenCookieProperties;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    private final RefreshTokenCookieProperties refreshTokenCookieProperties;

    public CookieService(RefreshTokenCookieProperties refreshTokenCookieProperties) {
        this.refreshTokenCookieProperties = refreshTokenCookieProperties;
    }

    public Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie(refreshTokenCookieProperties.getName(), refreshToken);
        cookie.setHttpOnly(refreshTokenCookieProperties.isHttpOnly());
        cookie.setSecure(refreshTokenCookieProperties.isSecure());
        cookie.setPath(refreshTokenCookieProperties.getPath());
        cookie.setMaxAge(refreshTokenCookieProperties.getMaxAge());
        return cookie;
    }
}