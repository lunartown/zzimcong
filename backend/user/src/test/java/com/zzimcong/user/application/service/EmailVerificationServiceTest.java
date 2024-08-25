package com.zzimcong.user.application.service;

import com.zzimcong.user.common.exception.ConflictException;
import com.zzimcong.user.common.exception.UnauthorizedException;
import com.zzimcong.user.common.util.RedisUtil;
import com.zzimcong.user.infrastructure.config.EmailProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EmailVerificationServiceTest {

    @Mock
    private EmailProperties emailProperties;

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    @Mock
    private RedisUtil redisUtil;

    @InjectMocks
    private EmailVerificationService emailVerificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendVerificationEmail() {
        when(userService.isEmailAvailable("test@example.com")).thenReturn(true);
        when(emailService.generateVerificationCode()).thenReturn("123456");

        emailVerificationService.sendVerificationEmail("test@example.com");

        verify(emailService, times(1)).sendVerificationEmail(eq("test@example.com"), anyString());
        verify(redisUtil, times(1)).setDataExpire(anyString(), eq("test@example.com"), anyLong());
    }

    @Test
    void testSendVerificationEmailDuplicateEmail() {
        when(userService.isEmailAvailable("test@example.com")).thenReturn(false);

        assertThrows(ConflictException.class, () -> emailVerificationService.sendVerificationEmail("test@example.com"));

        verify(emailService, never()).sendVerificationEmail(anyString(), anyString());
        verify(redisUtil, never()).setDataExpire(anyString(), anyString(), anyLong());
    }

    @Test
    void testVerifyEmailAndGenerateToken() {
        when(redisUtil.getData("123456")).thenReturn("test@example.com");

        String token = emailVerificationService.verifyEmailAndGenerateToken("test@example.com", "123456");

        assertNotNull(token);
        verify(redisUtil, times(1)).getData("123456");
    }

    @Test
    void testVerifyEmailAndGenerateTokenInvalidCode() {
        when(redisUtil.getData("123456")).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> emailVerificationService.verifyEmailAndGenerateToken("test@example.com", "123456"));

        verify(redisUtil, times(1)).getData("123456");
    }

    @Test
    void testValidateTokenForRegistration() {
        String email = "test@example.com";
        String token = "valid-token";

        emailVerificationService.validateTokenForRegistration(email, token);

        // 이 메서드가 예외를 던지지 않고 정상적으로 완료되면 테스트는 성공입니다.
    }

    @Test
    void testValidateTokenForRegistrationInvalidToken() {
        String email = "test@example.com";
        String token = "invalid-token";

        assertThrows(UnauthorizedException.class, () -> emailVerificationService.validateTokenForRegistration(email, token));
    }

    @Test
    void testIsEmailAvailable() {
        when(userService.isEmailAvailable("test@example.com")).thenReturn(true);

        boolean result = emailVerificationService.isEmailAvailable("test@example.com");

        assertTrue(result);
        verify(userService, times(1)).isEmailAvailable("test@example.com");
    }

    @Test
    void testIsEmailAvailableFalse() {
        when(userService.isEmailAvailable("test@example.com")).thenReturn(false);

        boolean result = emailVerificationService.isEmailAvailable("test@example.com");

        assertFalse(result);
        verify(userService, times(1)).isEmailAvailable("test@example.com");
    }
}
