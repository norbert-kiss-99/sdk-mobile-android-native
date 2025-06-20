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

public class PhoneWidget extends EditableWidget {

    private final WidgetModel.PhoneWidgetModel widgetModel;

    protected EditText inputView;

    protected TextView labelView;

    protected TextView errorLabelView;

    public PhoneWidget(Context context, WidgetModel.PhoneWidgetModel widgetModel) {
        super(context);
        this.widgetModel = widgetModel;

        // Creating the parent layout

        LinearLayout parentLayoutView = new LinearLayout(context);
        parentLayoutView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        parentLayoutView.setOrientation(LinearLayout.VERTICAL);

        // Creating the input

        inputView = new EditText(context);
        inputView.setInputType(InputType.TYPE_CLASS_PHONE);
        // autofill with the user's full phone number with country code
        inputView.setAutofillHints("phoneNumber");

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

    public final boolean isReadonly() {
        return widgetModel.isReadonly();
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
