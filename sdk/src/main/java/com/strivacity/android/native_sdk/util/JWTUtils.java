package com.strivacity.android.native_sdk.util;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class JWTUtils {

    public static JSONObject decoded(String JWTEncoded) {
        String[] parts = JWTEncoded.split("\\.");
        try {
            return new JSONObject(getJson(parts[1]));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getJson(String strEncoded) {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
