package com.strivacity.android.native_sdk.render.widgets;

import android.content.Context;

import lombok.Getter;

@Getter
public abstract class EditableWidget extends Widget {

    private boolean readonly;
    private boolean valid = true;

    public EditableWidget(Context context) {
        super(context);
    }

    public abstract Object getValue();

    public void clearError() {
        valid = true;
    }

    public void showError(String message) {
        valid = false;
    }
}
