package com.zzimcong.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
public class EmailSenderTest {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void testSendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("zzimcong99@gmail.com");
        message.setTo("recipient@example.com");
        message.setSubject("Test Subject");
        message.setText("Test Body");

        try {
            mailSender.send(message);
            System.out.println("Test email sent successfully");
        } catch (MailException e) {
            System.err.println("Failed to send test email. Error: " + e.getMessage());
            e.printStackTrace();

            if (e.getCause() != null) {
                System.err.println("Root cause: " + e.getCause().getMessage());
                e.getCause().printStackTrace();
            }
        }
    }
}