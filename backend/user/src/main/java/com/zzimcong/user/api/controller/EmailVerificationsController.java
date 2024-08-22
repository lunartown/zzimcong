package com.zzimcong.user.api.controller;

import com.zzimcong.user.application.dto.EmailCheckResponse;
import com.zzimcong.user.application.dto.EmailRequest;
import com.zzimcong.user.application.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "EMAIL_VERIFICATIONS_CONTROLLER")
@RestController
@RequestMapping("/api/v1/email-verifications")
public class EmailVerificationsController {
    private final EmailVerificationService emailVerificationService;

    @Autowired
    public EmailVerificationsController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody @Valid EmailRequest emailDto) {
        log.info("인증 이메일 전송 요청: {}", emailDto.email());
        emailVerificationService.sendVerificationEmail(emailDto.email());
        return ResponseEntity.ok("인증 이메일이 성공적으로 전송되었습니다.");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestBody @Valid EmailCheckResponse emailCheckResponse) {
        log.info("이메일 인증 요청: {}", emailCheckResponse.email());
        String token = emailVerificationService.verifyEmailAndGenerateToken(emailCheckResponse.email(), emailCheckResponse.authNum());
        return ResponseEntity.ok(token);
    }
}