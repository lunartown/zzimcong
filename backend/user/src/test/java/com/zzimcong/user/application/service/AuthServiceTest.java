package com.zzimcong.user.application.service;

import com.zzimcong.user.application.dto.AuthResponse;
import com.zzimcong.user.application.dto.LoginRequest;
import com.zzimcong.user.application.dto.SignupRequest;
import com.zzimcong.user.common.exception.UnauthorizedException;
import com.zzimcong.user.common.util.SecurityUtil;
import com.zzimcong.user.infrastructure.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserService userService;

    @Mock
    private EmailVerificationService emailVerificationService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUp() {
        SignupRequest signupRequest = new SignupRequest("test@example.com", "test", "010-0000-0000", "password");

        doNothing().when(emailVerificationService).validateTokenForRegistration(anyString(), anyString());
        when(userService.createUser(any(SignupRequest.class))).thenReturn(null);

        assertDoesNotThrow(() -> authService.signUp(signupRequest, "token"));

        verify(emailVerificationService, times(1)).validateTokenForRegistration(anyString(), anyString());
        verify(userService, times(1)).createUser(any(SignupRequest.class));
    }

    @Test
    void testLogin() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = User.withUsername("1").password("password").roles("USER").build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateAccessToken(anyLong())).thenReturn("accessToken");
        when(jwtUtil.generateRefreshToken(anyLong())).thenReturn("refreshToken");

        AuthResponse authResponse = authService.login(loginRequest);

        assertNotNull(authResponse);
        assertEquals("accessToken", authResponse.accessToken());
        assertEquals("refreshToken", authResponse.refreshToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, times(1)).generateAccessToken(anyLong());
        verify(jwtUtil, times(1)).generateRefreshToken(anyLong());
        verify(tokenService, times(1)).storeRefreshToken(anyString(), anyString());
    }

    @Test
    void testLogout() {
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getCurrentUserId).thenReturn(1L);

            assertDoesNotThrow(() -> authService.logout());

            verify(tokenService, times(1)).deleteRefreshToken("1");
        }
    }

    @Test
    void testRefreshToken() {
        String refreshToken = "validRefreshToken";
        when(jwtUtil.validateToken(refreshToken)).thenReturn(true);
        when(jwtUtil.extractId(refreshToken)).thenReturn(1L);
        when(jwtUtil.generateAccessToken(1L)).thenReturn("newAccessToken");
        when(jwtUtil.generateRefreshToken(1L)).thenReturn("newRefreshToken");

        AuthResponse result = authService.refreshToken(refreshToken);

        assertNotNull(result);
        assertEquals("newAccessToken", result.accessToken());
        assertEquals("newRefreshToken", result.refreshToken());

        verify(jwtUtil, times(1)).validateToken(refreshToken);
        verify(jwtUtil, times(1)).extractId(refreshToken);
        verify(jwtUtil, times(1)).generateAccessToken(1L);
        verify(jwtUtil, times(1)).generateRefreshToken(1L);
        verify(tokenService, times(1)).storeRefreshToken("1", "newRefreshToken");
    }

    @Test
    void testRefreshTokenInvalid() {
        String refreshToken = "invalidRefreshToken";
        when(jwtUtil.validateToken(refreshToken)).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.refreshToken(refreshToken));

        verify(jwtUtil, times(1)).validateToken(refreshToken);
        verify(jwtUtil, never()).extractId(anyString());
        verify(jwtUtil, never()).generateAccessToken(anyLong());
    }
}