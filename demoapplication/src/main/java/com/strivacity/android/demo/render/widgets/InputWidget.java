package com.strivacity.android.demo.render.widgets;

import static com.strivacity.android.demo.render.modifiers.FocusEvent.addFocusEventBehavior;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.strivacity.android.demo.render.constants.Colors;
import com.strivacity.android.demo.render.constants.Dimensions;
import com.strivacity.android.demo.render.constants.Drawables;
import com.strivacity.android.demo.render.modifiers.OptionalWidget;
import com.strivacity.android.native_sdk.render.models.WidgetModel;

public class InputWidget extends com.strivacity.android.native_sdk.render.widgets.InputWidget {

    public InputWidget(Context context, WidgetModel.InputWidgetModel widgetModel) {
        super(context, widgetModel);
        final float scaledDensityRatio = Dimensions.getScaledDensityRatio(context);

        // Remove the already added views to be able to add a new designed views

        ViewGroup parent = (ViewGroup) inputView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        // Modify the parent layout

        LinearLayout parentLayoutView = this.typedView();
        parentLayoutView.setOrientation(LinearLayout.VERTICAL);

        // Creating the layout for the input

        RelativeLayout inputLayoutView = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.ALIGN_BASELINE);
        inputLayoutView.setLayoutParams(layoutParams);

        // Modify the inputView

        inputView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        inputView.setPadding(
            Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPaddingTop * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio)
        );
        inputView.setSingleLine();
        inputView.setTextSize(Dimensions.textSizeMedium);
        inputView.setTextColor(Color.parseColor(Colors.textColor));
        inputView.setHintTextColor(Color.parseColor(Colors.textColor));
        inputView.setBackground(Drawables.getInputDrawable());

        // Creating the label for the input

        labelView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
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

        // Modifying the label for the input

        SpannableStringBuilder spannable = OptionalWidget.optionalWidgetSpannable(
            widgetModel.getLabel(),
            widgetModel.getValidator().isRequired()
        );
        labelView.setText(spannable);

        if (!TextUtils.isEmpty(widgetModel.getValue())) {
            inputView.setText(widgetModel.getValue());
            labelView.setTranslationY(-Dimensions.toPixel(Dimensions.inputFocusTranslateY * scaledDensityRatio));
            labelView.setTextSize(Dimensions.textSizeSmall);
        }

        // Creating the error label below the input

        errorLabelView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        errorLabelView.setPadding(0, 0, 0, Dimensions.toPixel(Dimensions.paddingSmall));
        errorLabelView.setTextSize(Dimensions.textSizeSmall);
        errorLabelView.setTextColor(Color.parseColor(Colors.danger));

        // Adding views

        inputLayoutView.addView(inputView);
        inputLayoutView.addView(labelView);

        parentLayoutView.addView(inputLayoutView);
        parentLayoutView.addView(errorLabelView);
        setView(parentLayoutView);

        // Adding focus functionality

        addFocusEventBehavior(context, inputView, isValid(), labelView);
    }

    @Override
    public void clearError() {
        super.clearError();
        inputView.setBackground(Drawables.getInputDrawable());
        labelView.setTextColor(Color.parseColor(Colors.textColor));
    }

    @Override
    public void showError(String message) {
        super.showError(message);
        inputView.setBackground(Drawables.getInputErrorDrawable());
        labelView.setTextColor(Color.parseColor(Colors.danger));
    }
}
