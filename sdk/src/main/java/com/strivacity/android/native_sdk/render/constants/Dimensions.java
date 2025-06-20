package com.strivacity.android.native_sdk.render.constants;

import android.content.Context;
import android.content.res.Resources;

public class Dimensions {

    // Note: Text sizes are defined in sp

    public static final float textSizeSmall = 16;
    public static final float textSizeMedium = 18;

    // Note: Sizes are defined in dp, it needs to be converted to pixels before usage

    public static final float inputFocusTranslateY = 16;

    public static int toPixel(float dp) {
        // 0.5f rounds up the value, because we cast this floating number to an integer
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }

    public static float getScaledDensityRatio(Context context) {
        return (
            context.getResources().getDisplayMetrics().scaledDensity / Resources.getSystem().getDisplayMetrics().density
        );
    }
}
