package com.zzimcong.user.common.util;

import com.zzimcong.user.common.exception.BadRequestException;
import com.zzimcong.user.common.exception.ErrorCode;
import com.zzimcong.user.common.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j(topic = "AESUtil")
@Component
public class AESUtil {
    private static final String ALGORITHM = "AES";
    private static final String CIPHER_INSTANCE = "AES/ECB/PKCS5Padding";  // 명시적으로 모드와 패딩 지정

    private final KeyManagementUtil keyManagementUtil;

    @Autowired
    public AESUtil(KeyManagementUtil keyManagementUtil) {
        this.keyManagementUtil = keyManagementUtil;
    }

    public String encrypt(String data) {
        if(data == null) throw new BadRequestException(ErrorCode.INVALID_EMAIL);
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.ENCRYPT_MODE, getKeySpec());
            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            log.error("Encryption failed", e);
            throw new InternalServerErrorException(ErrorCode.ENCRYPTION_FAILED);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, getKeySpec());
            byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Decryption failed", e);
            throw new InternalServerErrorException(ErrorCode.DECRYPTION_FAILED);
        }
    }

    private SecretKeySpec getKeySpec() {
        String key = keyManagementUtil.getEncryptionKey();
        byte[] decodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(decodedKey, ALGORITHM);
    }
}