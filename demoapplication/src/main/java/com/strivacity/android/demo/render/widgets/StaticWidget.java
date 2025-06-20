package com.strivacity.android.demo.render.widgets;

import static com.strivacity.android.native_sdk.render.constants.Dimensions.toPixel;

import android.content.Context;
import android.view.ViewGroup;

import com.strivacity.android.demo.render.constants.Dimensions;
import com.strivacity.android.native_sdk.render.models.WidgetModel;

public class StaticWidget extends com.strivacity.android.native_sdk.render.widgets.StaticWidget {

    public StaticWidget(Context context, WidgetModel.StaticWidgetModel staticWidgetModel) {
        super(context, staticWidgetModel);
        textView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        textView.setPadding(
            toPixel(Dimensions.paddingMedium),
            toPixel(Dimensions.paddingMedium),
            toPixel(Dimensions.paddingMedium),
            toPixel(Dimensions.paddingMedium)
        );
        textView.setTextSize(Dimensions.textSizeMedium);
    }
}
