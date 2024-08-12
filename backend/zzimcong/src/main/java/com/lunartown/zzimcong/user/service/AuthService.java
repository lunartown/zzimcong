package com.lunartown.zzimcong.user.service;

import com.lunartown.zzimcong.user.dto.AuthResultDto;
import com.lunartown.zzimcong.user.dto.LoginRequestDto;
import com.lunartown.zzimcong.user.dto.SignupRequestDto;
import com.lunartown.zzimcong.user.entity.User;
import com.lunartown.zzimcong.user.exception.InvalidCredentialsException;
import com.lunartown.zzimcong.user.exception.UnauthorizedException;
import com.lunartown.zzimcong.user.response.AccessTokenResponse;
import com.lunartown.zzimcong.user.response.AuthResponse;
import com.lunartown.zzimcong.user.security.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    public AuthResponse signUp(SignupRequestDto signupRequestDto, String token) {
        emailVerificationService.validateTokenForRegistration(signupRequestDto.getEmail(), token);
        User savedUser = userService.createUser(signupRequestDto);
        return generateAuthResponse(savedUser.getId());
    }

    public AuthResultDto login(LoginRequestDto requestDto) {
        try {
            Authentication authentication = authenticateUser(requestDto);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long id = Long.parseLong(userDetails.getUsername());
            return generateAuthResultDto(id);
        } catch (BadCredentialsException e) {
            log.error("로그인 실패: 잘못된 자격 증명", e);
            throw new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다.");
        } catch (Exception e) {
            log.error("로그인 중 예상치 못한 오류 발생", e);
            throw new RuntimeException("로그인 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
        }
    }

    public void logout(String accessToken) {
        try {
            if (!jwtUtil.validateToken(accessToken)) {
                throw new UnauthorizedException("유효하지 않은 토큰입니다.");
            }

            Long id = jwtUtil.extractId(accessToken);
            tokenService.deleteRefreshToken(String.valueOf(id));
            log.debug("logout: {}", id);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("만료된 토큰입니다.");
        }
    }

    public AccessTokenResponse refreshToken(String refreshToken) throws BadRequestException {
        if (refreshToken == null) {
            throw new BadRequestException("리프레시 토큰이 없습니다.");
        }

        try {
            if (!jwtUtil.validateToken(refreshToken)) {
                throw new UnauthorizedException("유효하지 않은 토큰입니다.");
            }

            Long id = jwtUtil.extractId(refreshToken);
            userService.getUserById(id); // 사용자 존재 여부 확인
            return new AccessTokenResponse(jwtUtil.generateAccessToken(id));
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("만료된 토큰입니다.");
        }
    }

    private Authentication authenticateUser(LoginRequestDto requestDto) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
        );
    }

    private AuthResponse generateAuthResponse(Long id) {
        String accessToken = jwtUtil.generateAccessToken(id);
        String refreshToken = jwtUtil.generateRefreshToken(id);
        tokenService.storeRefreshToken(String.valueOf(id), refreshToken);
        return new AuthResponse(accessToken, refreshToken);
    }

    private AuthResultDto generateAuthResultDto(Long id) {
        String accessToken = jwtUtil.generateAccessToken(id);
        String refreshToken = jwtUtil.generateRefreshToken(id);
        tokenService.storeRefreshToken(String.valueOf(id), refreshToken);
        return new AuthResultDto(accessToken, refreshToken);
    }
}