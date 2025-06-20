package com.strivacity.android.demo.render.modifiers;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

import com.strivacity.android.demo.render.constants.Colors;
import com.strivacity.android.demo.render.constants.Dimensions;
import com.strivacity.android.demo.render.constants.Drawables;

public class FocusEvent {

    public static void addFocusEventBehavior(Context context, EditText inputView, boolean isValid, TextView labelView) {
        final float fontSizeSmall = Dimensions.textSizeSmall;
        final float fontSizeMedium = Dimensions.textSizeMedium;
        final float inputFocusTranslateY = Dimensions.toPixel(
            Dimensions.inputFocusTranslateY * Dimensions.getScaledDensityRatio(context)
        );

        final int textColor = Color.parseColor(Colors.textColor);
        final int primaryColor = Color.parseColor(Colors.primary);
        final int errorColor = Color.parseColor(Colors.danger);

        inputView.setOnFocusChangeListener((v, focused) -> {
            ObjectAnimator animation;

            if (focused) {
                inputView.setBackground(Drawables.getInputFocusDrawable());

                PropertyValuesHolder textColorProperty = PropertyValuesHolder.ofInt(
                    "textColor",
                    isValid ? textColor : errorColor,
                    primaryColor
                );
                textColorProperty.setEvaluator(new ArgbEvaluator());

                if (inputView.length() > 0) {
                    animation = ObjectAnimator.ofPropertyValuesHolder(labelView, textColorProperty);
                } else {
                    animation =
                        ObjectAnimator.ofPropertyValuesHolder(
                            labelView,
                            PropertyValuesHolder.ofFloat("translationY", -inputFocusTranslateY),
                            PropertyValuesHolder.ofFloat("textSize", fontSizeMedium, fontSizeSmall),
                            textColorProperty
                        );
                }
            } else {
                if (isValid) {
                    inputView.setBackground(Drawables.getInputDrawable());
                } else {
                    inputView.setBackground(Drawables.getInputErrorDrawable());
                }

                PropertyValuesHolder textColorProperty = PropertyValuesHolder.ofInt(
                    "textColor",
                    primaryColor,
                    isValid ? textColor : errorColor
                );
                textColorProperty.setEvaluator(new ArgbEvaluator());

                if (inputView.length() > 0) {
                    animation = ObjectAnimator.ofPropertyValuesHolder(labelView, textColorProperty);
                } else {
                    animation =
                        ObjectAnimator.ofPropertyValuesHolder(
                            labelView,
                            PropertyValuesHolder.ofFloat("translationY", 0),
                            PropertyValuesHolder.ofFloat("textSize", fontSizeSmall, fontSizeMedium),
                            textColorProperty
                        );
                }
            }

            animation.setDuration(150).start();
        });
    }
}
