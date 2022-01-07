package com.sms.eagle.eye.backend.common.encrypt;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "eagle.encrypt")
@Slf4j
@Validated
public class AesEncryptor {

    public static final String ALGORITHM = "AES";
    public static final String SUFFIX = "=";
    private static final String AES_CBC_ALGORITHM = "AES/CBC/PKCS5Padding";
    @NotBlank
    @Size(min = 16, max = 16, message = "The secret key size must be equal to 16.")
    private String secretKey;

    public static String base64StringEncoding(byte[] source, boolean padded) {
        String encodeString = Base64.getEncoder().encodeToString(source);

        //No padding base64
        if (!padded) {
            if (encodeString.endsWith(SUFFIX)) {
                encodeString = encodeString.substring(0, encodeString.length() - 1);
                if (encodeString.endsWith(SUFFIX)) {
                    encodeString = encodeString.substring(0, encodeString.length() - 1);
                }
            }
        }
        return encodeString;
    }

    public static byte[] base64StringDecoding(String source) {
        return Base64.getDecoder().decode(source);
    }

    public static byte[] aes128Cbcstringencoding(String encData, String secretKey) throws Exception {

        if (StringUtils.isBlank(secretKey) || secretKey.length() != 16) {
            return null;
        }
        Cipher cipher = Cipher.getInstance(AES_CBC_ALGORITHM);
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, ALGORITHM);
        // use cbc iv offset to enhance the alg
        IvParameterSpec iv = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
        return cipher.doFinal(encData.getBytes(StandardCharsets.UTF_8));
    }

    public static String aes128Cbcstringdecoding(byte[] source, String secretKey)
        throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
        InvalidAlgorithmParameterException, InvalidKeyException {
        if (StringUtils.isBlank(secretKey) || secretKey.length() != 16) {
            return null;
        }
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.US_ASCII);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_CBC_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
        byte[] original = cipher.doFinal(source);
        return new String(original, StandardCharsets.UTF_8);
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String encrypt(String data) {
        try {
            return base64StringEncoding(aes128Cbcstringencoding(data, this.secretKey), false);
        } catch (Exception ex) {
            log.warn("Failed to encrypt key={}, error={}", data, ex);
            return null;
        }
    }

    public String decrypt(String data) {
        try {
            return aes128Cbcstringdecoding(base64StringDecoding(data), this.secretKey);
        } catch (Exception ex) {
            log.warn("Failed to decrypt key={}, error={}", data, ex);
            return null;
        }
    }


}
