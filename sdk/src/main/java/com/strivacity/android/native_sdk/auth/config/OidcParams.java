package com.strivacity.android.native_sdk.auth.config;

import com.strivacity.android.native_sdk.util.OIDCParamGenerator;

import lombok.Data;

import java.security.NoSuchAlgorithmException;

@Data
public class OidcParams {

    private String codeVerifier;
    private String codeChallenge;
    private String state;
    private String nonce;

    public OidcParams() throws NoSuchAlgorithmException {
        codeVerifier = OIDCParamGenerator.generateRandomString(32);
        codeChallenge = OIDCParamGenerator.generateCodeChallenge(codeVerifier);
        state = OIDCParamGenerator.generateRandomString(16);
        nonce = OIDCParamGenerator.generateRandomString(16);
    }
}
