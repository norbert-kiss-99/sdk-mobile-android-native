package com.strivacity.android.native_sdk.render.widgets;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strivacity.android.native_sdk.render.constants.Colors;
import com.strivacity.android.native_sdk.render.models.WidgetModel;

public class CheckboxWidget extends EditableWidget {

    private final WidgetModel.CheckboxWidgetModel widgetModel;

    protected final TextView checkboxView;
    protected final TextView errorLabelView;

    public CheckboxWidget(Context context, WidgetModel.CheckboxWidgetModel widgetModel) {
        super(context);
        this.widgetModel = widgetModel;

        // Creating the parent layout

        LinearLayout parentLayoutView = new LinearLayout(context);
        parentLayoutView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        parentLayoutView.setOrientation(LinearLayout.VERTICAL);

        // Creating the CheckBox for receiving input

        switch (widgetModel.getRender().getType()) {
            case "checkboxShown":
                CheckBox checkboxView = new CheckBox(context);
                checkboxView.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                );

                checkboxView.setEnabled(!widgetModel.isReadonly());
                checkboxView.setChecked(widgetModel.isValue());

                this.checkboxView = checkboxView;
                break;
            case "checkboxHidden":
                TextView textView = new TextView(context);
                textView.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                );

                this.checkboxView = textView;
                break;
            default:
                throw new RuntimeException("Unknown render type: " + widgetModel.getRender().getType());
        }

        checkboxView.setText(
            "html".equals(widgetModel.getRender().getLabelType())
                ? Html.fromHtml(widgetModel.getLabel(), Html.FROM_HTML_MODE_COMPACT)
                : widgetModel.getLabel()
        );

        // Creating the error label below the input

        errorLabelView = new TextView(context);
        errorLabelView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        errorLabelView.setTextColor(Color.parseColor(Colors.danger));

        // Adding views

        parentLayoutView.addView(checkboxView);
        parentLayoutView.addView(errorLabelView);

        setView(parentLayoutView);
    }

    public final boolean isReadonly() {
        return widgetModel.isReadonly();
    }

    @Override
    public Boolean getValue() {
        if (checkboxView instanceof CheckBox) {
            return ((CheckBox) checkboxView).isChecked();
        }

        return true;
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
