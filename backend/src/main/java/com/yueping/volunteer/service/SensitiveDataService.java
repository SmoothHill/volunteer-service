package com.yueping.volunteer.service;

import com.yueping.volunteer.properties.SensitiveDataProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Service
public class SensitiveDataService {

    private static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String AES_KEY_ALGORITHM = "AES";
    private static final String ENCRYPTED_PREFIX = "ENC:";

    private static String encryptionKey;
    private static String hashSalt;

    private final SensitiveDataProperties properties;

    public SensitiveDataService(SensitiveDataProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void validateConfiguration() {
        encryptionKey = requireConfiguredValue(properties.getEncryptionKey(), "APP_SENSITIVE_ENCRYPTION_KEY");
        hashSalt = requireConfiguredValue(properties.getHashSalt(), "APP_SENSITIVE_HASH_SALT");
    }

    public static String encrypt(String plainText) {
        String normalized = normalize(plainText);
        if (!StringUtils.hasText(normalized)) {
            return "";
        }
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, buildSecretKey());
            byte[] encrypted = cipher.doFinal(normalized.getBytes(StandardCharsets.UTF_8));
            return ENCRYPTED_PREFIX + Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new IllegalStateException("Sensitive data encryption failed", ex);
        }
    }

    public static String decrypt(String cipherText) {
        String normalized = normalize(cipherText);
        if (!StringUtils.hasText(normalized)) {
            return "";
        }
        if (!isEncryptedValue(normalized)) {
            return normalized;
        }
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, buildSecretKey());
            byte[] decoded = Base64.getDecoder().decode(normalized.substring(ENCRYPTED_PREFIX.length()));
            return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new IllegalStateException("Sensitive data decryption failed", ex);
        }
    }

    public static String hashForLookup(String plainText) {
        String normalized = normalize(plainText);
        if (!StringUtils.hasText(normalized)) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest((hashSalt + ":" + normalized).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Sensitive data hash failed", ex);
        }
    }

    public static boolean isEncryptedValue(String value) {
        return StringUtils.hasText(value) && normalize(value).startsWith(ENCRYPTED_PREFIX);
    }

    public static String encryptIfNeeded(String value) {
        String normalized = normalize(value);
        if (!StringUtils.hasText(normalized)) {
            return "";
        }
        return isEncryptedValue(normalized) ? normalized : encrypt(normalized);
    }

    public static String decryptIfNeeded(String value) {
        String normalized = normalize(value);
        if (!StringUtils.hasText(normalized)) {
            return "";
        }
        return isEncryptedValue(normalized) ? decrypt(normalized) : normalized;
    }

    private static SecretKeySpec buildSecretKey() {
        byte[] keyBytes = Arrays.copyOf(encryptionKey.getBytes(StandardCharsets.UTF_8), 16);
        return new SecretKeySpec(keyBytes, AES_KEY_ALGORITHM);
    }

    private String requireConfiguredValue(String value, String envName) {
        if (StringUtils.hasText(value)) {
            return value.trim();
        }
        throw new IllegalStateException("Missing " + envName);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
