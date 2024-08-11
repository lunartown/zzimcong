package com.lunartown.zzimcong.user.controller;

import com.lunartown.zzimcong.user.dto.EmailCheckDto;
import com.lunartown.zzimcong.user.dto.EmailRequestDto;
import com.lunartown.zzimcong.user.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "EMAIL_VERIFICATIONS_CONTROLLER")
@RestController
@RequestMapping("/api/v1/email-verifications")
@RequiredArgsConstructor
public class EmailVerificationsController {
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody @Valid EmailRequestDto emailDto) {
        log.info("인증 이메일 전송 요청: {}", emailDto.getEmail());
        boolean isEmailSent = emailVerificationService.sendVerificationEmail(emailDto.getEmail());
        if (isEmailSent) {
            return ResponseEntity.ok("인증 이메일이 성공적으로 전송되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 이메일 전송에 실패했습니다.");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestBody @Valid EmailCheckDto emailCheckDto) {
        log.info("이메일 인증 요청: {}", emailCheckDto.getEmail());
        String token = emailVerificationService.verifyEmailAndGenerateToken(emailCheckDto.getEmail(), emailCheckDto.getAuthNum());
        return ResponseEntity.ok(token);
    }
}