package com.strivacity.android.native_sdk.auth;

import androidx.annotation.NonNull;

import com.strivacity.android.native_sdk.util.JWTUtils;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;

@Data
@FieldNameConstants
@NoArgsConstructor
public class Session {

    private String accessToken;
    private Instant expiration; // of accessToken
    private String idToken;
    private String refreshToken;

    @Setter(AccessLevel.NONE)
    private IdTokenClaims idTokenClaims;

    public Session(String data) {
        try {
            JSONObject json = new JSONObject(data);
            this.accessToken = json.getString(Fields.accessToken);
            this.expiration = Instant.parse(json.getString(Fields.expiration));

            if (!json.isNull(Fields.refreshToken)) {
                this.refreshToken = json.getString(Fields.refreshToken);
            }

            if (!json.isNull(Fields.idToken)) {
                setIdToken(json.getString(Fields.idToken));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
        this.idTokenClaims = new IdTokenClaims(JWTUtils.decoded(idToken));
    }

    @NonNull
    @Override
    public String toString() {
        try {
            JSONObject json = new JSONObject();
            json.put(Fields.accessToken, this.accessToken);
            json.put(Fields.idToken, this.idToken);
            json.put(Fields.refreshToken, this.refreshToken);
            json.put(Fields.expiration, this.expiration.toString());

            return json.toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
