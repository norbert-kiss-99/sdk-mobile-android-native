package com.strivacity.android.demo.render.widgets;

import static com.strivacity.android.demo.render.modifiers.FocusEvent.addFocusEventBehavior;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.ViewGroup;

import com.strivacity.android.demo.render.constants.Colors;
import com.strivacity.android.demo.render.constants.Dimensions;
import com.strivacity.android.demo.render.constants.Drawables;
import com.strivacity.android.native_sdk.render.models.WidgetModel;

public class PasswordWidget extends com.strivacity.android.native_sdk.render.widgets.PasswordWidget {

    private static final float inputPaddingRight = 44;

    public PasswordWidget(Context context, WidgetModel.PasswordWidgetModel widgetModel) {
        super(context, widgetModel);
        final float scaledDensityRatio = Dimensions.getScaledDensityRatio(context);

        // Recreate views

        ViewGroup parentLayout = this.typedView();
        parentLayout.removeAllViews();

        ViewGroup inputViewLayout = (ViewGroup) inputView.getParent();
        inputViewLayout.removeAllViews();

        // Modify the input

        inputView.setPadding(
            Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPaddingTop * scaledDensityRatio),
            Dimensions.toPixel(inputPaddingRight * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio)
        );
        inputView.setSingleLine();
        inputView.setTextSize(Dimensions.textSizeMedium);
        inputView.setTextColor(Color.parseColor(Colors.textColor));
        inputView.setHintTextColor(Color.parseColor(Colors.textColor));
        inputView.setBackground(Drawables.getInputDrawable());
        inputView.setTransformationMethod(PasswordTransformationMethod.getInstance());

        // Modify the label for the input

        labelView.setPadding(
            Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPaddingVertical * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPaddingVertical * scaledDensityRatio)
        );
        labelView.setSingleLine();
        labelView.setEllipsize(TextUtils.TruncateAt.END);
        labelView.setTextSize(Dimensions.textSizeMedium);
        labelView.setTextColor(Color.parseColor(Colors.textColor));

        // Modify the password toggle image

        passwordToggleView.setPadding(
            Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPaddingVertical * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPaddingVertical * scaledDensityRatio)
        );
        passwordToggleView.setColorFilter(Color.parseColor(Colors.textColor));

        // Modify the error label below the input

        errorLabelView.setPadding(0, 0, 0, Dimensions.toPixel(Dimensions.paddingSmall));
        errorLabelView.setTextSize(Dimensions.textSizeSmall);

        // Add views

        inputLayoutView.addView(inputView);
        inputLayoutView.addView(labelView);
        inputLayoutView.addView(passwordToggleView);
        parentLayout.addView(inputLayoutView);
        parentLayout.addView(errorLabelView);

        // Adding focus functionality

        addFocusEventBehavior(context, inputView, isValid(), labelView);
    }
}
