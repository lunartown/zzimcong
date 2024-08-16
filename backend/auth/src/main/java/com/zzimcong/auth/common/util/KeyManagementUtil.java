package com.zzimcong.auth.common.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class KeyManagementUtil {

    private final Environment environment;
    @Getter
    private String encryptionKey;

    @Autowired
    public KeyManagementUtil(Environment environment) {
        this.environment = environment;
        initEncryptionKey();
    }

    private void initEncryptionKey() {
        // 시스템 환경 변수에서 먼저 확인
        encryptionKey = System.getenv("ENCRYPTION_KEY");

        // 시스템 환경 변수에 없으면 스프링 Environment에서 확인
        if (encryptionKey == null || encryptionKey.isEmpty()) {
            encryptionKey = environment.getProperty("encryption.key");
        }

        // 둘 다 없으면 예외 발생
        if (encryptionKey == null || encryptionKey.isEmpty()) {
            throw new IllegalStateException("ENCRYPTION_KEY가 설정되지 않았습니다.");
        }
    }

}