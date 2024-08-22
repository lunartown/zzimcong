package com.zzimcong.user.application.service;

import com.zzimcong.user.application.dto.AuthResponse;
import com.zzimcong.user.application.dto.LoginRequest;
import com.zzimcong.user.application.dto.SignupRequest;
import com.zzimcong.user.common.exception.ErrorCode;
import com.zzimcong.user.common.exception.UnauthorizedException;
import com.zzimcong.user.common.util.SecurityUtil;
import com.zzimcong.user.infrastructure.security.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j(topic = "AUTH_SERVICE")
@Service
public class AuthService {
    private final TokenService tokenService;
    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(TokenService tokenService,
                       UserService userService,
                       EmailVerificationService emailVerificationService,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.emailVerificationService = emailVerificationService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public void signUp(SignupRequest signupRequest, String token) {
        emailVerificationService.validateTokenForRegistration(signupRequest.email(), token);
        userService.createUser(signupRequest);
    }

    public AuthResponse login(LoginRequest requestDto) {
        try {
            Authentication authentication = authenticateUser(requestDto);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long id = Long.parseLong(userDetails.getUsername());
            return generateAuthResultDto(id);
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_CREDENTIALS, "잘못된 이메일 또는 비밀번호입니다.");
        }
    }

    public void logout() {
        Long id = SecurityUtil.getCurrentUserId();
        try {
            tokenService.deleteRefreshToken(String.valueOf(id));
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_TOKEN);
        }
        log.info("User {} logged out successfully", id);
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new UnauthorizedException(ErrorCode.TOKEN_NOT_FOUND);
        }
        try {
            if (!jwtUtil.validateToken(refreshToken)) {
                throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
            }
            Long id = jwtUtil.extractId(refreshToken);
            return generateAuthResultDto(id);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_TOKEN);
        }
    }

    private Authentication authenticateUser(LoginRequest requestDto) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.email(), requestDto.password())
        );
    }

    private AuthResponse generateAuthResultDto(Long id) {
        String accessToken = jwtUtil.generateAccessToken(id);
        String refreshToken = jwtUtil.generateRefreshToken(id);
        tokenService.storeRefreshToken(String.valueOf(id), refreshToken);
        return new AuthResponse(accessToken, refreshToken);
    }
}