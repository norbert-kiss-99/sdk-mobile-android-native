package com.strivacity.android.native_sdk.render.widgets;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Widget {

    private View view;

    protected Context context;

    public Widget(Context context) {
        this.context = context;
    }

    public <T> T typedView() {
        return (T) view;
    }

    public void render(ViewGroup parentLayout) {
        parentLayout.addView(view);
    }
}
