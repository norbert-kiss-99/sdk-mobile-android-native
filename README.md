![Strivacity Android SDK](https://static.strivacity.com/images/android-native-sdk-banner.png)

See our [Developer Portal](https://www.strivacity.com/learn-support/developer-hub) to get started with developing for the Strivacity product.

# Overview

This SDK allows you to integrate Strivacity's policy-driven journeys into your brand's Android mobile application using native mobile experiences via [Journey-flow API for native clients](https://docs.strivacity.com/reference/journey-flow-api-for-native-clients).

The SDK uses the [PKCE extension to OAuth](https://tools.ietf.org/html/rfc7636) to ensure the secure exchange of authorization codes in public clients.

## How to use

Strivacity SDK for Android is available on [MavenCentral](https://search.maven.org/search?q=g:com.strivacity.android%20AND%20a:native_sdk).

```groovy
implementation 'com.strivacity.android:native_sdk:<version>'
```

## Demo Application

A demo application is available in the `demoapplication` folder.

## Overview

The Strivacity SDK for Android provides the possibility to build an application which can communicate with Strivacity using OAuth 2.0 PKCE flow.

## Instantiate and initialize Native SDK

First, in your `Activity` you must create a NativeSDK instance:

```java
TenantConfiguration tenantConfiguration = new TenantConfiguration(
        Uri.parse("<issuer-url>"),              // specifies authentication server domain, e.g.: https://your-domain.tld
        "<client-id>",                          // specifies OAuth2 client ID
        Uri.parse("<redirect-uri>"),            // specifies the redirect uri, e.g.: android://native-flow
        Uri.parse("<post-logout-uri>")          // specifies the post logout uri, e.g.: android://native-flow
);

NativeSDK nativeSDK =
   new NativeSDK(
       tenantConfiguration,
       new ViewFactory(this),                           // An instance of com.strivacity.android.native_sdk.render.ViewFactory for rendering
       new CookieManager(),                             // An instance of java.net.CookieManager for storing cookies between calls
       this.getSharedPreferences("test", MODE_PRIVATE)  // An instance of android.content.SharedPreferences for storing tokens and claims
   );
```

## Register the custom schema

The custom schema used in the redirect and post logout uri's needs to be registered for your application.
Create an `intent-filter` xml tag in your `AndroidManifest.xml` file in one of your `activity` tags.
Set the same `schema` and `host` parameters provided in the `TenantConfiguration`.

For example:
```xml
<activity android:name=".RedirectActivity"
   android:exported="true">
   <intent-filter>
       <action android:name="android.intent.action.VIEW" />

       <category android:name="android.intent.category.DEFAULT" />
       <category android:name="android.intent.category.BROWSABLE" />

       <data android:host="native-flow" android:scheme="android" />
   </intent-filter>
</activity>
```

Create an `Activity` to handle the call from the custom schema and pass the required information back to you primary `Activity`
For example:
```java
public class RedirectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setData(getIntent().getData());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);
        finish();
    }
}
```

In your primary `Activity` provide an implementation for the `onResume` method and call the `continueFlow` method on the `nativeSDK` instance.

```java
 @Override
 protected void onResume() {
     super.onResume();
     if (getIntent().getData() != null) {
         nativeSDK.continueFlow(getIntent().getData());
     }
 }
```

## How to launch a login flow

Login flow can be launched using the `login` method on the `nativeSDK` instance.

```swift
void login(
     LoginParameters loginParameters,        // additional parameters to pass through during login
     ViewGroup parentLayout,                 // the parent layout where the login flow should be rendered
     Consumer<IdTokenClaims> onSuccess,      // callback method that will be called after a successful login
     Consumer<Throwable> onError             // callback method that will be called if an error occures
 )
```

The following additional parameters can be set:
```swift
public class LoginParameters {

    private final String prompt;             // sets the corresponding parameter in the OAuth2 authorize call
    private final String loginHint;          // sets the corresponding parameter in the OAuth2 authorize call
    private final List<String> acrValues;    // sets the corresponding parameter in the OAuth2 authorize call
    private final List<String> scopes;       // sets the corresponding parameter in the OAuth2 authorize call

    private String uiLocales = Locale.getDefault().toLanguageTag();  // sets the language of the flow
}
```

To cancel an on-going flow the `cancelFlow` method can be used on the `nativeSDK` instance.

### Handling a logged-in session

The `getIdTokenClaims` method can be used on the `nativeSDK` instance to check if there is a logged in session already it will return `null` in case there is none.
If there is an existing session the method above will give back the claims.

The access token can be retrieved using the `getAccessToken` method on the `nativeSDK` instance.

To validate if the current session's access token is still valid, the `isAuthenticated` method can be called on the `nativeSDK` instance. This call will also try to refresh the access token, if a refresh token is available.

To trigger a logout the `logout` method can be called on the `nativeSDK` instance.

## Author

Strivacity: [opensource@strivacity.com](mailto:opensource@strivacity.com)

## License

Strivacity is available under the Apache License, Version 2.0. See the [LICENSE](./LICENSE) file for more info.

## Vulnerability Reporting

The [Guidelines for responsible disclosure](https://www.strivacity.com/report-a-security-issue) details the procedure for disclosing security issues.
Please do not report security vulnerabilities on the public issue tracker.
