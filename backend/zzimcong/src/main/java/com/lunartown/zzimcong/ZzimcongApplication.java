package com.lunartown.zzimcong;

import com.lunartown.zzimcong.user.config.EmailProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(EmailProperties.class)
public class ZzimcongApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZzimcongApplication.class, args);
    }
}
