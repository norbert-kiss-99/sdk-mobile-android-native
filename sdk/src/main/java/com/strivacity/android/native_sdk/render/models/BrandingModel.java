package com.strivacity.android.native_sdk.render.models;

import com.strivacity.android.native_sdk.util.JSON;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class BrandingModel {

    private final String logoUrl;
    private final String copyright;
    private final String siteTermUrl;
    private final String privacyPolicyUrl;
    private final Styling styling = new Styling();

    public BrandingModel(JSON json) {
        this.logoUrl = json.string("logoUrl");
        this.copyright = json.string("copyright");
        this.siteTermUrl = json.string("siteTermUrl");
        this.privacyPolicyUrl = json.string("privacyPolicyUrl");
    }

    public static class Styling {}
}
