package com.zzimcong.auth.application.service;

import com.zzimcong.auth.common.exception.ConflictException;
import com.zzimcong.auth.common.exception.ErrorCode;
import com.zzimcong.auth.common.exception.UnauthorizedException;
import com.zzimcong.auth.common.util.EmailVerificationToken;
import com.zzimcong.auth.common.util.RedisUtil;
import com.zzimcong.auth.infrastructure.config.EmailProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j(topic = "EMAIL_VERIFICATION_SERVICE")
@Service
public class EmailVerificationService {
    private final Set<EmailVerificationToken> emailVerificationTokens =
            Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final EmailProperties emailProperties;
    private final EmailService emailService;
    private final UserService userService;
    private final RedisUtil redisUtil;

    public EmailVerificationService(EmailProperties emailProperties, EmailService emailService, UserService userService, RedisUtil redisUtil) {
        this.emailProperties = emailProperties;
        this.emailService = emailService;
        this.userService = userService;
        this.redisUtil = redisUtil;
    }

    public void sendVerificationEmail(String email) {
        if (!isEmailAvailable(email)) {
            log.warn("사용할 수 없는 이메일입니다: {}", email);
            throw new ConflictException(ErrorCode.DUPLICATE_EMAIL);
        }
        String verificationCode = emailService.generateVerificationCode();
        emailService.sendVerificationEmail(email, verificationCode);
        storeVerificationCode(verificationCode, email);
        log.info("인증 이메일 전송: {}", email);
    }

    public boolean isEmailAvailable(String email) {
        return userService.checkEmailAvailability(email);
    }

    private void storeVerificationCode(String code, String email) {
        redisUtil.setDataExpire(code, email,
                emailProperties.getVerification().getCode().getExpiry().getSeconds());
    }

    public String verifyEmailAndGenerateToken(String email, String verificationCode) {
        if (!verifyEmailCode(email, verificationCode)) {
            throw new UnauthorizedException(ErrorCode.INVALID_VERIFICATION_CODE);
        }
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken =
                new EmailVerificationToken(token, email, LocalDateTime.now().plusMinutes(
                        emailProperties.getVerification().getToken().getExpiry().getMinutes()));
        emailVerificationTokens.add(verificationToken);
        log.info("이메일 인증 성공 및 토큰 생성: {}", email);
        return token;
    }

    private boolean verifyEmailCode(String email, String code) {
        String storedEmail = redisUtil.getData(code);
        return storedEmail != null && storedEmail.equals(email);
    }

    public void validateTokenForRegistration(String email, String token) {
        EmailVerificationToken verificationToken = emailVerificationTokens.stream()
                .filter(t -> t.getToken().trim().equalsIgnoreCase(token.trim())
                        && t.getEmail().trim().equalsIgnoreCase(email.trim()))
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_TOKEN));

        if (LocalDateTime.now().isAfter(verificationToken.getExpiryDate())) {
            emailVerificationTokens.remove(verificationToken);
            throw new UnauthorizedException(ErrorCode.EXPIRED_TOKEN);
        }

        emailVerificationTokens.remove(verificationToken);
    }

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void cleanupExpiredTokens() {
        emailVerificationTokens.removeIf(token -> LocalDateTime.now().isAfter(token.getExpiryDate()));
    }
}