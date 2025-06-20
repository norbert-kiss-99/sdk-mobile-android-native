package com.strivacity.android.native_sdk.render.widgets;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strivacity.android.native_sdk.render.constants.Colors;
import com.strivacity.android.native_sdk.render.models.WidgetModel;

public class PasscodeWidget extends EditableWidget {

    protected EditText inputView;
    protected TextView labelView;

    protected TextView errorLabelView;

    public PasscodeWidget(Context context, WidgetModel.PasscodeWidgetModel widgetModel) {
        super(context);
        // Creating the parent layout

        LinearLayout parentLayoutView = new LinearLayout(context);
        parentLayoutView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        parentLayoutView.setOrientation(LinearLayout.VERTICAL);

        // Creating the input

        /* For one-time passcodes (API 26+), View.AUTOFILL_HINT_SMS_OTP not working,
           but this string literal does the same, and the constant support only API 30+ */
        inputView = new EditText(context);
        inputView.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputView.setAutofillHints("smsOTPCode");

        // Creating the label for the input

        labelView = new TextView(context);
        labelView.setText(widgetModel.getLabel());

        errorLabelView = new TextView(context);
        errorLabelView.setTextColor(Color.parseColor(Colors.danger));

        parentLayoutView.addView(labelView);
        parentLayoutView.addView(inputView);
        parentLayoutView.addView(errorLabelView);
        setView(parentLayoutView);
    }

    @Override
    public Object getValue() {
        return inputView.getText() == null ? null : inputView.getText().toString();
    }

    @Override
    public void clearError() {
        super.clearError();

        getContext()
            .getMainExecutor()
            .execute(() -> {
                errorLabelView.setText(null);
            });
    }

    @Override
    public void showError(String message) {
        super.showError(message);

        getContext()
            .getMainExecutor()
            .execute(() -> {
                errorLabelView.setText(message);
            });
    }
}
