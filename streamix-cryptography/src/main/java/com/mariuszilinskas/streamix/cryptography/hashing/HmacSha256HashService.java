package com.mariuszilinskas.streamix.cryptography.hashing;

import com.mariuszilinskas.streamix.cryptography.exception.CryptographyException;
import com.mariuszilinskas.streamix.cryptography.service.DeterministicHashService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.HexFormat;

public final class HmacSha256HashService implements DeterministicHashService {

    private static final String ALGORITHM = "HmacSHA256";

    private final SecretKeySpec secretKey;

    public HmacSha256HashService(String base64EncodedKey) {
        if (base64EncodedKey == null || base64EncodedKey.isBlank()) {
            throw new IllegalArgumentException("HMAC key must not be null or blank");
        }

        byte[] keyBytes;

        try {
            keyBytes = Base64.getDecoder().decode(base64EncodedKey);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("HMAC key must be valid Base64", e);
        }

        this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
    }

    @Override
    public String hash(String value) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }

        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKey);
            byte[] hmacBytes = mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hmacBytes);
        } catch (GeneralSecurityException e) {
            throw new CryptographyException("HMAC-SHA256 hashing failed", e);
        }
    }

}
