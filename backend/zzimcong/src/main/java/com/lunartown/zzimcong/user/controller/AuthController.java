package com.lunartown.zzimcong.user.controller;

import com.lunartown.zzimcong.api.response.ErrorResponse;
import com.lunartown.zzimcong.api.response.SuccessResponse;
import com.lunartown.zzimcong.user.dto.*;
import com.lunartown.zzimcong.user.exception.AuthException;
import com.lunartown.zzimcong.user.exception.UnauthorizedException;
import com.lunartown.zzimcong.user.response.AccessTokenResponse;
import com.lunartown.zzimcong.user.service.AuthService;
import com.lunartown.zzimcong.user.service.EmailVerificationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmailAvailability(@RequestBody EmailRequestDto emailRequestDto) {
        try {
            boolean isAvailable = emailVerificationService.isEmailAvailable(emailRequestDto.getEmail());
            if (isAvailable) {
                return ResponseEntity.ok(new SuccessResponse("사용 가능한 이메일입니다."));
            } else {
                return ResponseEntity.ok(new ErrorResponse("이미 사용 중인 이메일입니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("이메일 확인 중 오류가 발생했습니다."));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignupRequestDto signupRequestDto,
                                    @RequestParam String token) {
        try {
            authService.signUp(signupRequestDto, token);
            return ResponseEntity.ok(new SuccessResponse("회원가입이 완료되었습니다."));
        } catch (AuthException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
        try {
            AuthResultDto authResultDto = authService.login(loginRequestDto);
            return ResponseEntity.ok().body(authResultDto);
        } catch (AuthException e) {
            log.error("Authentication failed for email: {}", loginRequestDto.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인에 실패했습니다.");
        } catch (Exception e) {
            log.error("Unexpected error during authentication", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) {
        authService.logout(logoutRequest.getAccessToken());
        return ResponseEntity.ok().build();
    }

    // refreshToken을 이용한 accessToken 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequestDto refreshRequestDto) {
        String refreshToken = refreshRequestDto.getRefreshToken();
        try {
            AccessTokenResponse response = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
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