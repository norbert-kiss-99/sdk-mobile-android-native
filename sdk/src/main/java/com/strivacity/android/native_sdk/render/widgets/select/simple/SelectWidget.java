package com.strivacity.android.native_sdk.render.widgets.select.simple;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strivacity.android.native_sdk.render.constants.Colors;
import com.strivacity.android.native_sdk.render.models.WidgetModel.SelectWidgetModel;
import com.strivacity.android.native_sdk.render.widgets.EditableWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class SelectWidget extends EditableWidget {

    protected static final String OPTIONS_ARE_EMPTY = "Options is an empty array";
    protected final SelectWidgetModel widgetModel;
    protected TextView errorLabelView;
    protected static final String NOT_A_VALID_SELECT_OPTION_TYPE = "Not a valid option type in the select widget";
    protected final LinearLayout parentLayoutView;
    private View childView;

    public SelectWidget(Context context, SelectWidgetModel widgetModel) {
        super(context);
        this.widgetModel = widgetModel;

        // Creating the parent layout
        parentLayoutView = new LinearLayout(context);
        parentLayoutView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        );
        parentLayoutView.setOrientation(LinearLayout.VERTICAL);

        // Adding views
        super.setView(parentLayoutView);
    }

    public final boolean isReadonly() {
        return widgetModel.isReadonly();
    }

    @Override
    public void clearError() {
        super.clearError();
        getContext().getMainExecutor().execute(() -> errorLabelView.setText(null));
    }

    @Override
    public void showError(String message) {
        super.showError(message);
        getContext().getMainExecutor().execute(() -> errorLabelView.setText(message));
    }

    @Override
    public void setView(View view) {
        childView = view;

        parentLayoutView.removeAllViews();
        parentLayoutView.addView(view);

        // Add error label to the view
        errorLabelView = new TextView(context);
        errorLabelView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        errorLabelView.setTextColor(Color.parseColor(Colors.danger));
        parentLayoutView.addView(errorLabelView);
    }

    @Override
    public <T> T typedView() {
        return (T) childView;
    }

    protected static List<SelectWidgetModel.Option> flatten(List<SelectWidgetModel.Option> options) {
        List<SelectWidgetModel.Option> flattened = new ArrayList<>();

        // We de not support nested groups above 2 level
        for (SelectWidgetModel.Option option : options) {
            if (!(Objects.equals(option.getType(), "group") || Objects.equals(option.getType(), "item"))) {
                throw new RuntimeException(NOT_A_VALID_SELECT_OPTION_TYPE);
            }

            flattened.add(option);

            if (option.getOptions() != null) {
                flattened.addAll(option.getOptions());
            }
        }

        return flattened;
    }
}
