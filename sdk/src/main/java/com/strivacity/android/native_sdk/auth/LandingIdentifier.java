package com.strivacity.android.native_sdk.auth;

import java.util.Arrays;

public enum LandingIdentifier {
    MAGIC_LINK_EXPIRED("magicLinkExpired"),

    CLIENT_MISMATCH("clientMismatch"),
    INVALID_REDIRECT_URI("invalidRedirectUri");

    private final String id;

    LandingIdentifier(String id) {
        this.id = id;
    }

    public static LandingIdentifier valueOfId(String id) {
        return Arrays
            .stream(values())
            .filter(landingIdentifier -> landingIdentifier.id.equals(id))
            .findFirst()
            .orElse(null);
    }
}
