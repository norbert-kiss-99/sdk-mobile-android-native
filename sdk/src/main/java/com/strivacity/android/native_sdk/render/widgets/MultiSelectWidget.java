package com.strivacity.android.native_sdk.render.widgets;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strivacity.android.native_sdk.render.constants.Colors;
import com.strivacity.android.native_sdk.render.models.WidgetModel.MultiSelectWidgetModel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MultiSelectWidget extends EditableWidget {

    protected static final String NOT_A_VALID_SELECT_OPTION_TYPE = "Not a valid option type in the select widget";
    private static final String OPTIONS_ARE_EMPTY = "Options is an empty array";
    protected final TextView errorLabelView;
    protected final List<String> selectedItems = new ArrayList<>();
    protected final List<MultiSelectWidgetModel.Option> flattenedOptions;
    protected final TextView multiSelectLabel;
    protected final List<CheckBoxContainer> checkBoxContainers;
    protected final List<GroupTitleContainer> groupTitleContainers;
    private final MultiSelectWidgetModel widgetModel;

    public MultiSelectWidget(Context context, MultiSelectWidgetModel widgetModel) {
        super(context);
        this.widgetModel = widgetModel;

        // Creating the parent layout
        LinearLayout parentLayoutView = new LinearLayout(context);
        parentLayoutView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        );
        parentLayoutView.setOrientation(LinearLayout.VERTICAL);

        // Create label for multi select
        multiSelectLabel = new TextView(context);
        multiSelectLabel.setText(widgetModel.getLabel());
        parentLayoutView.addView(multiSelectLabel);

        List<MultiSelectWidgetModel.Option> widgetOptions = widgetModel.getOptions();
        if (!widgetModel.getOptions().isEmpty()) {
            boolean hasGroup = widgetModel.getOptions().stream().anyMatch(option -> option.getType().equals("group"));
            if (hasGroup) {
                flattenedOptions = MultiSelectWidget.flatten(widgetOptions);
            } else {
                flattenedOptions = widgetModel.getOptions();
            }
        } else {
            throw new RuntimeException(OPTIONS_ARE_EMPTY);
        }

        ArrayList<CheckBoxContainer> checkBoxContainers = new ArrayList<>();
        ArrayList<GroupTitleContainer> groupTitleContainers = new ArrayList<>();

        for (MultiSelectWidgetModel.Option option : flattenedOptions) {
            // Checkbox layout
            LinearLayout checkboxLayoutView = new LinearLayout(context);
            checkboxLayoutView.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            );

            checkboxLayoutView.setLayoutParams(layoutParams);

            if (Objects.equals(option.getType(), "item")) {
                //Checkbox
                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(option.getLabel());
                checkBoxContainers.add(new CheckBoxContainer(checkboxLayoutView, checkBox));

                if (widgetModel.getValue().contains(option.getValue())) {
                    checkBox.setChecked(true);
                    selectedItems.add(option.getValue());
                }

                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    checkedChangeListener(checkBox, checkboxLayoutView, option.getValue());
                });

                checkboxLayoutView.addView(checkBox);
            } else if (Objects.equals(option.getType(), "group")) {
                TextView groupLabel = new TextView(context);
                groupLabel.setText(option.getLabel());
                checkboxLayoutView.addView(groupLabel);
                groupTitleContainers.add(new GroupTitleContainer(checkboxLayoutView, groupLabel));
            } else {
                throw new RuntimeException(NOT_A_VALID_SELECT_OPTION_TYPE);
            }

            // Add listener to handle selection
            parentLayoutView.addView(checkboxLayoutView);
        }

        this.checkBoxContainers = List.copyOf(checkBoxContainers);
        this.groupTitleContainers = List.copyOf(groupTitleContainers);

        // Add error label to the view
        errorLabelView = new TextView(context);
        errorLabelView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        errorLabelView.setTextColor(Color.parseColor(Colors.danger));
        parentLayoutView.addView(errorLabelView);

        // Adding views
        super.setView(parentLayoutView);
    }

    protected void checkedChangeListener(CheckBox checkbox, LinearLayout layout, String value) {
        if (checkbox.isChecked()) {
            selectedItems.add(value);
        } else {
            selectedItems.remove(value);
        }
    }

    public final boolean isReadonly() {
        return widgetModel.isReadonly();
    }

    @Override
    public List<String> getValue() {
        return selectedItems;
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

    protected static List<MultiSelectWidgetModel.Option> flatten(List<MultiSelectWidgetModel.Option> options) {
        List<MultiSelectWidgetModel.Option> flattened = new ArrayList<>();

        // We de not support nested groups above 2 level
        for (MultiSelectWidgetModel.Option option : options) {
            if (!(Objects.equals(option.getType(), "group") || Objects.equals(option.getType(), "item"))) {
                throw new RuntimeException();
            }

            flattened.add(option);

            if (option.getOptions() != null) {
                flattened.addAll(option.getOptions());
            }
        }

        return flattened;
    }

    @Data
    protected static class CheckBoxContainer {

        private final LinearLayout layout;
        private final CheckBox checkbox;
    }

    @Data
    protected static class GroupTitleContainer {

        private final LinearLayout layout;
        private final TextView groupTitle;
    }
}
