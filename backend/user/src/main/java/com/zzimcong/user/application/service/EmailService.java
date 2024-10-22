package com.zzimcong.user.application.service;

import com.zzimcong.user.common.exception.InternalServerErrorException;
import com.zzimcong.user.infrastructure.config.EmailProperties;
import com.zzimcong.user.common.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Slf4j(topic = "EMAIL_SERVICE")
@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailProperties emailProperties;
    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;

    public String generateVerificationCode() {
        Random random = new Random();
        return String.format("%0" + emailProperties.getVerification().getCode().getLength() + "d",
                random.nextInt((int) Math.pow(10, emailProperties.getVerification().getCode().getLength())));
    }

    private String prepareEmailContent(String verificationCode) {
        try {
            String template = loadEmailTemplate();
            return template.replace("${verificationCode}", verificationCode);
        } catch (IOException e) {
            log.error("Failed to load email template", e);
            throw new InternalServerErrorException(ErrorCode.RESOURCE_LOAD_FAILED,
                    "이메일 템플릿 로딩에 실패했습니다.");
        }
    }

    private String loadEmailTemplate() throws IOException {
        Resource resource = resourceLoader.getResource(emailProperties.getTemplate().getPath());
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    public void sendVerificationEmail(String toEmail, String verificationCode) {
        try {
            String content = prepareEmailContent(verificationCode);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setFrom(emailProperties.getSender());
            helper.setTo(toEmail);
            helper.setSubject(emailProperties.getSubject());
            helper.setText(content, true);

            mailSender.send(message);
            log.info("인증 메일이 성공적으로 전송되었습니다: {}", toEmail);
        } catch (MessagingException e) {
            log.error("인증 메일 전송에 실패했습니다: {}", toEmail, e);
            throw new InternalServerErrorException(ErrorCode.EMAIL_SEND_FAILED,
                    "인증 메일 전송에 실패했습니다.");
        }
    }
}