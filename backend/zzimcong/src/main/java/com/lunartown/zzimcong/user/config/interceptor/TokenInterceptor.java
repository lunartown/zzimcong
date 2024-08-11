package com.lunartown.zzimcong.user.config.interceptor;

import com.lunartown.zzimcong.user.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final CookieService cookieService;

    public TokenInterceptor(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String refreshToken = (String) request.getAttribute("refreshToken");
        if (refreshToken != null) {
            Cookie refreshTokenCookie = cookieService.createRefreshTokenCookie(refreshToken);
            response.addCookie(refreshTokenCookie);
        }
    }
}