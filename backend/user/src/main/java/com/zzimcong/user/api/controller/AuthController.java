package com.zzimcong.user.api.controller;

import com.zzimcong.user.api.response.ApiResponse;
import com.zzimcong.user.application.dto.*;
import com.zzimcong.user.application.service.AuthService;
import com.zzimcong.user.application.service.EmailVerificationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "AUTH_CONTROLLER")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;

    @Autowired
    public AuthController(AuthService authService, EmailVerificationService emailVerificationService) {
        this.authService = authService;
        this.emailVerificationService = emailVerificationService;
    }

    @GetMapping("/info")
    public String getUserInfo(HttpServletRequest request) {
        return "User Info";
    }

    // 이메일 중복 확인
    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmailAvailability(@RequestBody EmailRequest emailRequest) {
        boolean isAvailable = emailVerificationService.isEmailAvailable(emailRequest.email());
        String message = isAvailable ? "사용 가능한 이메일입니다." : "이미 사용 중인 이메일입니다.";
        return ResponseEntity.ok(ApiResponse.success(isAvailable, message));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignupRequest signupRequest,
                                    @RequestParam String token) {
        authService.signUp(signupRequest, token);
        return ResponseEntity.ok(ApiResponse.success(null, "회원가입이 완료되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(authResponse, "로그인에 성공했습니다."));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok(ApiResponse.success(null, "로그아웃되었습니다."));
    }

    // refreshToken을 이용한 accessToken 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.refreshToken();
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success(authResponse, "토큰이 갱신되었습니다."));
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}