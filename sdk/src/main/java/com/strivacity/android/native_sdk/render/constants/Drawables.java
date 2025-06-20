package com.strivacity.android.native_sdk.render.constants;

import static com.strivacity.android.native_sdk.render.constants.Dimensions.toPixel;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class Drawables {

    public static GradientDrawable getSelectedDropdownItemDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(Color.parseColor(Colors.inputBorderColor));
        drawable.setStroke(toPixel(1), Color.parseColor(Colors.inputBorderColor));
        return drawable;
    }
}
