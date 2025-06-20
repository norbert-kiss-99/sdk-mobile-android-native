package com.strivacity.android.native_sdk.render.widgets.select.simple;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.strivacity.android.native_sdk.render.models.WidgetModel.SelectWidgetModel;
import com.strivacity.android.native_sdk.render.models.WidgetModel.SelectWidgetModel.Option;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RadioWidget extends SelectWidget {

    protected final RadioGroup radioGroup;
    protected final List<RadioOption> radioOptionList = new ArrayList<>();
    protected final List<TextView> groupTitleList = new ArrayList<>();
    protected final TextView selectLabel;

    public RadioWidget(Context context, SelectWidgetModel widgetModel) {
        super(context, widgetModel);
        LinearLayout radioSelectView = new LinearLayout(context);
        radioSelectView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        radioSelectView.setOrientation(LinearLayout.VERTICAL);

        radioGroup = new RadioGroup(context);
        radioGroup.setLayoutParams(
            new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        RadioGroup.LayoutParams radioLayoutParams = (RadioGroup.LayoutParams) radioGroup.getLayoutParams();

        List<Option> flattenedOptions;
        if (!widgetModel.getOptions().isEmpty()) {
            boolean hasGroup = widgetModel.getOptions().stream().anyMatch(option -> option.getType().equals("group"));
            if (hasGroup) {
                flattenedOptions = RadioWidget.flatten(widgetModel.getOptions());
            } else {
                flattenedOptions = widgetModel.getOptions();
            }
        } else {
            throw new RuntimeException(OPTIONS_ARE_EMPTY);
        }

        //Set the label of the select
        selectLabel = new TextView(context);
        selectLabel.setText(widgetModel.getLabel());
        radioSelectView.addView(selectLabel);

        flattenedOptions.forEach(option -> {
            if (Objects.equals(option.getType(), "group")) {
                TextView groupLabel = new TextView(context);
                groupLabel.setText(option.getLabel());
                radioGroup.addView(groupLabel);
                groupTitleList.add(groupLabel);
            } else if (Objects.equals(option.getType(), "item")) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setLayoutParams(radioLayoutParams);
                radioButton.setText(option.getLabel());
                radioOptionList.add(new RadioOption(radioButton, option.getValue()));
                radioGroup.addView(radioButton);

                if (Objects.equals(option.getValue(), widgetModel.getValue())) {
                    radioGroup.check(radioButton.getId());
                }
            }
        });

        radioSelectView.addView(radioGroup);
        setView(radioSelectView);
    }

    @Override
    public String getValue() {
        Optional<RadioOption> selected = radioOptionList
            .stream()
            .filter(radioOption -> radioOption.radioButton.isChecked())
            .findFirst();
        return selected.map(radioOption -> radioOption.value).orElse(null);
    }

    @Override
    public void clearError() {
        super.clearError();
    }

    @Override
    public void showError(String message) {
        super.showError(message);
    }

    @Data
    protected static class RadioOption {

        final RadioButton radioButton;
        final String value;
    }
}
