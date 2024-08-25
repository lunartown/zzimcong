package com.zzimcong.user.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    private final EmailProperties emailProperties;

    public EmailConfig(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailProperties.getHost());
        mailSender.setPort(emailProperties.getPort());
        mailSender.setUsername(emailProperties.getUsername());
        mailSender.setPassword(emailProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", String.valueOf(emailProperties.getProperties().getMail().getSmtp().isAuth()));
        props.put("mail.smtp.starttls.enable", String.valueOf(
                emailProperties.getProperties().getMail().getSmtp().getStarttls().isEnable()));

        // 개발 환경에서만 디버그 모드 활성화
        if (System.getProperty("spring.profiles.active", "").contains("local")) {
            props.put("mail.debug", "true");
        }

        return mailSender;
    }
}