package com.lunartown.zzimcong.user.service;

import com.lunartown.zzimcong.common.util.EmailVerificationToken;
import com.lunartown.zzimcong.common.util.RedisUtil;
import com.lunartown.zzimcong.user.config.EmailProperties;
import com.lunartown.zzimcong.user.exception.InvalidTokenException;
import com.lunartown.zzimcong.user.exception.InvalidVerificationCodeException;
import com.lunartown.zzimcong.user.exception.TokenExpiredException;
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

    public boolean sendVerificationEmail(String email) {
        if (!isEmailAvailable(email)) {
            log.warn("사용할 수 없는 이메일입니다: {}", email);
            return false;
        }
        String verificationCode = emailService.generateVerificationCode();
        boolean isEmailSent = emailService.sendVerificationEmail(email, verificationCode);
        if (isEmailSent) {
            storeVerificationCode(verificationCode, email);
        }
        return isEmailSent;
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
            throw new InvalidVerificationCodeException("유효하지 않은 인증 코드입니다.");
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
                .orElseThrow(() -> new InvalidTokenException("유효하지 않은 이메일 검증 토큰입니다."));

        if (LocalDateTime.now().isAfter(verificationToken.getExpiryDate())) {
            emailVerificationTokens.remove(verificationToken);
            throw new TokenExpiredException("만료된 이메일 검증 토큰입니다.");
        }

        emailVerificationTokens.remove(verificationToken);
    }

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void cleanupExpiredTokens() {
        emailVerificationTokens.removeIf(token -> LocalDateTime.now().isAfter(token.getExpiryDate()));
    }
}