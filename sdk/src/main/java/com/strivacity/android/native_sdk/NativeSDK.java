package com.strivacity.android.native_sdk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.ViewGroup;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;

import com.strivacity.android.native_sdk.auth.Flow;
import com.strivacity.android.native_sdk.auth.IdTokenClaims;
import com.strivacity.android.native_sdk.auth.LandingIdentifier;
import com.strivacity.android.native_sdk.auth.NativeSDKError;
import com.strivacity.android.native_sdk.auth.Session;
import com.strivacity.android.native_sdk.auth.config.LoginParameters;
import com.strivacity.android.native_sdk.auth.config.TenantConfiguration;
import com.strivacity.android.native_sdk.render.Form;
import com.strivacity.android.native_sdk.render.ScreenRenderer;
import com.strivacity.android.native_sdk.render.ViewFactory;
import com.strivacity.android.native_sdk.util.HttpClient;

import lombok.Getter;

import java.net.CookieHandler;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class NativeSDK {

    private static final String STORE_KEY = "Strivacity";

    private static final String WORKFLOW_FINISHED_WIDGET_ID = "workflow-close";

    // Configuration
    private final TenantConfiguration tenantConfiguration;
    private final Executor backgroundThread;
    private final ViewFactory viewFactory;
    private final CookieHandler cookieHandler;
    private final SharedPreferences sharedPreferences;

    // Per-login
    private Flow flow;
    private ScreenRenderer screenRenderer;
    private Consumer<IdTokenClaims> onSuccess;
    private Consumer<Throwable> onError;
    private Runnable onFlowFinish;

    // Session data
    private Session session;

    @Getter
    private boolean workflowInProgress = false;

    @Getter
    private boolean fallback;

    public NativeSDK(
        TenantConfiguration tenantConfiguration,
        ViewFactory viewFactory,
        CookieHandler cookieHandler,
        SharedPreferences sharedPreferences
    ) {
        this.tenantConfiguration = tenantConfiguration;
        this.sharedPreferences = sharedPreferences;
        this.viewFactory = viewFactory;
        this.cookieHandler = cookieHandler;
        this.backgroundThread = Executors.newSingleThreadExecutor();

        if (sharedPreferences != null) {
            String data = sharedPreferences.getString(STORE_KEY, null);
            if (data != null) {
                this.session = new Session(data);
            }
        }
    }

    public IdTokenClaims getIdTokenClaims() {
        if (session == null) {
            return null;
        }

        return session.getIdTokenClaims();
    }

    public String getAccessToken() {
        if (session == null) {
            return null;
        }

        return session.getAccessToken();
    }

    @MainThread
    public void isAuthenticated(Consumer<Boolean> onResponse) {
        backgroundThread.execute(() -> {
            if (session == null) {
                executeOnMain(() -> onResponse.accept(false));
                return;
            }

            boolean hasValidAccessToken =
                session.getAccessToken() != null && session.getExpiration().isAfter(Instant.now());

            if (!hasValidAccessToken && session.getRefreshToken() != null) {
                try {
                    session = Flow.refreshToken(tenantConfiguration, cookieHandler, session.getRefreshToken());
                    if (sharedPreferences != null) {
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString(STORE_KEY, session.toString());
                        edit.apply();
                    }
                    hasValidAccessToken = true;
                } catch (Exception ignored) {}
            }

            boolean authenticated = hasValidAccessToken;

            if (!authenticated) {
                session = null;
            }
            executeOnMain(() -> onResponse.accept(authenticated));
        });
    }

    @MainThread
    public void login(
        LoginParameters loginParameters,
        ViewGroup parentLayout,
        Consumer<IdTokenClaims> onSuccess,
        Consumer<Throwable> onError
    ) {
        login(loginParameters, parentLayout, onSuccess, onError, () -> error(new NativeSDKError.HostedFlowCancelled()));
    }

    @MainThread
    public void login(
        LoginParameters loginParameters,
        ViewGroup parentLayout,
        Consumer<IdTokenClaims> onSuccess,
        Consumer<Throwable> onError,
        Runnable onFlowFinish
    ) {
        backgroundThread.execute(() -> {
            try {
                this.onSuccess = onSuccess;
                this.onError = onError;
                this.onFlowFinish = onFlowFinish;

                fallback = false;
                workflowInProgress = false;

                flow = new Flow(tenantConfiguration, cookieHandler);
                screenRenderer =
                    new ScreenRenderer(
                        viewFactory,
                        parentLayout,
                        this::submitForm,
                        finalizeUri -> {
                            HttpClient.HttpResponse finalizeResponse = flow.follow(finalizeUri);
                            continueFlow(Uri.parse(finalizeResponse.getHeader("Location")));
                        }
                    );
                Uri finalizeUri = flow.startSession(loginParameters);
                if (finalizeUri != null) {
                    continueFlow(finalizeUri);
                    return;
                }
            } catch (NativeSDKError.OIDCError oidcError) {
                error(oidcError);
                return;
            } catch (Exception e) {
                error(new NativeSDKError.UnknownError(e));
                return;
            }

            submitForm(null);
        });
    }

    @MainThread
    public void cancelFlow() {
        continueFlow(null);
    }

    @MainThread
    public void continueFlow(Uri redirectUri) {
        fallback = false;

        if (flow == null) {
            return;
        }

        if (redirectUri == null) {
            workflowInProgress = false;
            error(new NativeSDKError.HostedFlowCancelled());
            return;
        }

        String sessionId = redirectUri.getQueryParameter("session_id");
        if (sessionId != null) {
            this.refreshScreen();
            return;
        }

        backgroundThread.execute(() -> {
            try {
                String codeToken = redirectUri.getQueryParameter("code");
                String state = redirectUri.getQueryParameter("state");
                if (!Objects.equals(state, flow.getOidcParams().getState())) {
                    error(new NativeSDKError.OIDCError("Validation error", "State parameter mismatch"));
                    return;
                }

                session = flow.tokenExchange(codeToken);
                success(session.getIdTokenClaims());
            } catch (Exception e) {
                error(new NativeSDKError.UnknownError(e));
            }
        });
    }

    @MainThread
    public void entry(
        Uri uri,
        ViewGroup parentLayout,
        Runnable onFlowFinish,
        Consumer<Throwable> onError,
        Consumer<LandingIdentifier> onLanding
    ) {
        backgroundThread.execute(() -> {
            try {
                this.onError = onError;
                this.onFlowFinish = onFlowFinish;

                cleanUp();

                if (uri == null) {
                    error(new NativeSDKError.UnknownError(new RuntimeException("Entry URI is null")));
                    return;
                }

                String challenge = uri.getQueryParameter("challenge");
                if (challenge == null || challenge.trim().isEmpty()) {
                    throw new NativeSDKError.UnknownError(new RuntimeException("Entry challenge parameter is missing"));
                }

                workflowInProgress = true;

                flow = new Flow(tenantConfiguration, cookieHandler);
                screenRenderer = new ScreenRenderer(viewFactory, parentLayout, this::submitForm, finalizeUri -> {});

                boolean shouldStartFlow = flow.startWorkflow(
                    challenge,
                    fragment -> executeOnMain(() -> onLanding.accept(LandingIdentifier.valueOfId(fragment)))
                );
                if (!shouldStartFlow) {
                    return;
                }
            } catch (NativeSDKError.OIDCError oidcError) {
                error(oidcError);
                return;
            } catch (Exception e) {
                error(new NativeSDKError.UnknownError(e));
                return;
            }

            submitForm(null);
        });
    }

    @MainThread
    public void logout() {
        backgroundThread.execute(() -> {
            Flow.logout(tenantConfiguration, cookieHandler, session);
            session = null;
            if (sharedPreferences != null) {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.remove(STORE_KEY);
                edit.apply();
            }
        });
    }

    public void refreshScreen() {
        if (screenRenderer == null || flow == null) {
            return;
        }

        this.submitForm(null);
    }

    private void submitForm(@Nullable Form form) {
        backgroundThread.execute(() -> {
            HttpClient.HttpResponse httpResponse;

            if (form == null) {
                httpResponse = flow.initForm();
            } else {
                if (form.getWidgets().containsKey(WORKFLOW_FINISHED_WIDGET_ID) && onFlowFinish != null) {
                    cleanUp();

                    executeOnMain(() -> onFlowFinish.run());

                    return;
                }

                httpResponse = flow.submitForm(form.getId(), form.requestBody().toString());
            }

            renderScreen(httpResponse);
        });
    }

    private void renderScreen(HttpClient.HttpResponse httpResponse) {
        if (screenRenderer == null) {
            return;
        }

        try {
            this.screenRenderer.showScreen(httpResponse);
        } catch (Exception e) {
            executeOnMain(() -> {
                fallback = true;

                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                customTabsIntent.intent.setPackage("com.android.chrome");

                customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                customTabsIntent.launchUrl(viewFactory.getContext(), screenRenderer.getFallbackUrl());
            });
        }
    }

    private void success(@Nullable IdTokenClaims idTokenClaims) {
        cleanUp();

        if (sharedPreferences != null) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString(STORE_KEY, session.toString());
            edit.apply();
        }

        if (onSuccess != null) {
            executeOnMain(() -> onSuccess.accept(idTokenClaims));
        }
    }

    private void error(Throwable throwable) {
        cleanUp();

        if (onError != null) {
            executeOnMain(() -> onError.accept(throwable));
        }
    }

    private void cleanUp() {
        if (screenRenderer != null) {
            screenRenderer.clear();
            screenRenderer = null;
            flow = null;
        }

        fallback = false;
        workflowInProgress = false;
    }

    private void executeOnMain(Runnable runnable) {
        viewFactory.getContext().getMainExecutor().execute(runnable);
    }
}
