package com.lunartown.zzimcong.user.service;

import com.lunartown.zzimcong.user.dto.AuthResultDto;
import com.lunartown.zzimcong.user.dto.LoginRequestDto;
import com.lunartown.zzimcong.user.dto.SignupRequestDto;
import com.lunartown.zzimcong.user.exception.ErrorCode;
import com.lunartown.zzimcong.user.exception.UnauthorizedException;
import com.lunartown.zzimcong.user.security.jwt.JwtUtil;
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

    public void signUp(SignupRequestDto signupRequestDto, String token) {
        emailVerificationService.validateTokenForRegistration(signupRequestDto.getEmail(), token);
        userService.createUser(signupRequestDto);
    }

    public AuthResultDto login(LoginRequestDto requestDto) {
        try {
            Authentication authentication = authenticateUser(requestDto);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long id = Long.parseLong(userDetails.getUsername());
            return generateAuthResultDto(id);
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_CREDENTIALS, "잘못된 이메일 또는 비밀번호입니다.");
        }
    }

    public void logout(String accessToken) {
        try {
            if (!jwtUtil.validateToken(accessToken)) {
                throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
            }
            Long id = jwtUtil.extractId(accessToken);
            tokenService.deleteRefreshToken(String.valueOf(id));
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_TOKEN);
        }
    }

    public String refreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new UnauthorizedException(ErrorCode.TOKEN_NOT_FOUND);
        }
        try {
            if (!jwtUtil.validateToken(refreshToken)) {
                throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
            }
            Long id = jwtUtil.extractId(refreshToken);
            userService.getUserById(id);
            return jwtUtil.generateAccessToken(id);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_TOKEN);
        }
    }

    private Authentication authenticateUser(LoginRequestDto requestDto) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
        );
    }

    private AuthResultDto generateAuthResultDto(Long id) {
        String accessToken = jwtUtil.generateAccessToken(id);
        String refreshToken = jwtUtil.generateRefreshToken(id);
        tokenService.storeRefreshToken(String.valueOf(id), refreshToken);
        return new AuthResultDto(accessToken, refreshToken);
    }
}