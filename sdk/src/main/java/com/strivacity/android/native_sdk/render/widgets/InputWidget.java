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

public class InputWidget extends EditableWidget {

    private final WidgetModel.InputWidgetModel widgetModel;

    protected EditText inputView;

    protected TextView labelView;

    protected TextView errorLabelView;

    public InputWidget(Context context, WidgetModel.InputWidgetModel widgetModel) {
        super(context);
        this.widgetModel = widgetModel;

        // Creating the parent layout

        LinearLayout parentLayoutView = new LinearLayout(context);
        parentLayoutView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        parentLayoutView.setOrientation(LinearLayout.VERTICAL);

        labelView = new TextView(context);
        labelView.setText(widgetModel.getLabel());

        errorLabelView = new TextView(context);
        errorLabelView.setTextColor(Color.parseColor(Colors.danger));

        inputView = new EditText(context);

        if (widgetModel.getInputmode() != null) {
            switch (widgetModel.getInputmode()) {
                case "email":
                    inputView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    break;
                default:
                    throw new RuntimeException("Unknown input mode: " + widgetModel.getInputmode());
            }
        }

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
        errorLabelView.setText(null);
    }

    @Override
    public void showError(String message) {
        super.showError(message);
        errorLabelView.setText(message);
    }
}
