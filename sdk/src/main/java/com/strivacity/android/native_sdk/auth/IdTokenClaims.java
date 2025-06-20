package com.strivacity.android.native_sdk.auth;

import com.strivacity.android.native_sdk.util.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class IdTokenClaims {

    private final JSONObject jsonObject;

    private final Instant authenticationTime;
    private final Instant expirationTime;
    private final Instant issuedAt;
    private final String issuer;
    private final String nonce;
    private final String subject;
    private final List<String> audience;

    public IdTokenClaims(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        try {
            this.authenticationTime = Instant.ofEpochSecond(jsonObject.getInt("auth_time"));
            this.expirationTime = Instant.ofEpochSecond(jsonObject.getInt("exp"));
            this.issuedAt = Instant.ofEpochSecond(jsonObject.getInt("iat"));
            this.issuer = JSON.parseString(jsonObject, "iss");
            this.nonce = JSON.parseString(jsonObject, "nonce");
            this.subject = JSON.parseString(jsonObject, "sub");

            this.audience = JSON.parseStringList(jsonObject, "aud");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String getString(String key) {
        try {
            return JSON.parseString(jsonObject, key);
        } catch (JSONException e) {
            return null;
        }
    }
}
