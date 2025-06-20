package com.strivacity.android.native_sdk.auth;

import lombok.Getter;

public class NativeSDKError {

    @Getter
    public static class OIDCError extends RuntimeException {

        private final String error;
        private final String errorDescription;

        public OIDCError(String error, String errorDescription) {
            super(error + " - " + errorDescription);
            this.error = error;
            this.errorDescription = errorDescription;
        }
    }

    public static class HostedFlowCancelled extends RuntimeException {

        public HostedFlowCancelled() {
            super("Hosted flow has been cancelled");
        }
    }

    public static class UnknownError extends RuntimeException {

        public UnknownError(Throwable throwable) {
            super(throwable);
        }
    }
}
