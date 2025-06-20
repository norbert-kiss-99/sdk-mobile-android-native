package com.strivacity.android.demo.render.constants;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

public class Drawables {

    public static GradientDrawable getInputDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(Color.parseColor(Colors.inputBg));
        drawable.setStroke(Dimensions.toPixel(1), Color.parseColor(Colors.inputBorderColor));
        drawable.setCornerRadius(Dimensions.toPixel(Dimensions.borderRadius));
        return drawable;
    }

    public static GradientDrawable getInputFocusDrawable() {
        GradientDrawable drawable = getInputDrawable();
        drawable.setStroke(Dimensions.toPixel(1), Color.parseColor(Colors.primary));
        return drawable;
    }

    public static GradientDrawable getInputErrorDrawable() {
        GradientDrawable drawable = getInputDrawable();
        drawable.setStroke(Dimensions.toPixel(1), Color.parseColor(Colors.danger));
        return drawable;
    }

    public static GradientDrawable getSelectDropdownDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(Color.parseColor(Colors.inputBg));
        drawable.setStroke(Dimensions.toPixel(1), Color.parseColor(Colors.primary));
        drawable.setCornerRadius(Dimensions.toPixel(Dimensions.borderRadius));
        return drawable;
    }

    public static GradientDrawable getSelectedDropdownItemDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(Color.parseColor(Colors.palePrimary));
        drawable.setStroke(Dimensions.toPixel(1), Color.parseColor(Colors.primary));
        return drawable;
    }

    public static GradientDrawable getTransparentBackgroundDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.TRANSPARENT);
        return drawable;
    }

    public static StateListDrawable focusableInputBackgroundDrawable() {
        StateListDrawable backgroundDrawable = new StateListDrawable();
        backgroundDrawable.addState(new int[] { android.R.attr.state_focused }, Drawables.getInputFocusDrawable());
        backgroundDrawable.addState(new int[] {}, Drawables.getInputDrawable());
        return backgroundDrawable;
    }

    public static ColorStateList checkboxDrawable() {
        return new ColorStateList(
            new int[][] {
                new int[] { android.R.attr.state_checked }, // Checked state
                new int[] { -android.R.attr.state_checked }, // Unchecked state
            },
            new int[] {
                Color.parseColor(Colors.primary), // Color when checked
                Color.parseColor(Colors.primary), // Color when unchecked
            }
        );
    }

    public static ShapeDrawable multiSelectCheckboxDrawable() {
        /* 2 variables define 1 corner, from top left in clockwise
           first variable is the x, second is the y axis, so we can set elliptical corner radius too if we want
        */
        float[] radius = new float[] {
            Dimensions.borderRadius * 2,
            Dimensions.borderRadius * 2,
            Dimensions.borderRadius * 2,
            Dimensions.borderRadius * 2,
            Dimensions.borderRadius * 2,
            Dimensions.borderRadius * 2,
            Dimensions.borderRadius * 2,
            Dimensions.borderRadius * 2,
        };

        RoundRectShape roundRectShape = new RoundRectShape(radius, null, null);

        ShapeDrawable rectangleDrawable = new ShapeDrawable(roundRectShape);
        rectangleDrawable.getPaint().setColor(Color.parseColor(Colors.palePrimary)); // Set your desired background color
        rectangleDrawable.getPaint().setStyle(Paint.Style.FILL); // Fill the shape
        return rectangleDrawable;
    }
}
