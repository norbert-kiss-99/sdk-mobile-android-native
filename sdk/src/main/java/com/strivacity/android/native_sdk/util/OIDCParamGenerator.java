package com.strivacity.android.native_sdk.util;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class OIDCParamGenerator {

    public static String generateRandomString(int byteLengths) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[byteLengths];
        random.nextBytes(bytes);

        return Base64.encodeToString(bytes, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
    }

    public static String generateCodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeToString(hash, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
    }
}
