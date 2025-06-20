package com.strivacity.android.demo.render.widgets.select.simple;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.strivacity.android.demo.render.constants.Colors;
import com.strivacity.android.demo.render.constants.Dimensions;
import com.strivacity.android.demo.render.constants.Drawables;
import com.strivacity.android.native_sdk.render.models.WidgetModel.SelectWidgetModel;
import com.strivacity.android.native_sdk.render.models.WidgetModel.SelectWidgetModel.Option;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class DropdownWidget extends com.strivacity.android.native_sdk.render.widgets.select.simple.DropdownWidget {

    private final Spinner dropdownGroup;
    private final List<Option> flattenedOptions;

    public DropdownWidget(Context context, SelectWidgetModel widgetModel) {
        super(context, widgetModel);
        dropdownGroup = new Spinner(context);

        RelativeLayout.LayoutParams spinnerLayoutParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        dropdownGroup.setLayoutParams(spinnerLayoutParams);
        dropdownGroup.setPopupBackgroundDrawable(Drawables.getSelectDropdownDrawable());
        dropdownGroup.setBackground(Drawables.focusableInputBackgroundDrawable());

        List<Option> widgetOptions = widgetModel.getOptions();
        if (!widgetModel.getOptions().isEmpty()) {
            boolean hasGroup = widgetModel.getOptions().stream().anyMatch(option -> option.getType().equals("group"));
            if (hasGroup) {
                flattenedOptions = DropdownWidget.flatten(widgetOptions);
            } else {
                flattenedOptions = widgetModel.getOptions();
            }
        } else {
            throw new RuntimeException(OPTIONS_ARE_EMPTY);
        }

        Option selectLabelOption = new Option("item", widgetModel.getLabel(), null, null);
        flattenedOptions.add(0, selectLabelOption);

        ArrayAdapter<Option> adapter = new ArrayAdapter<Option>(context, 0, flattenedOptions) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                return getSelectView(this::createCustomTextView, position);
            }

            @Override
            public boolean isEnabled(int position) {
                return Objects.equals(flattenedOptions.get(position).getType(), "item");
            }

            @Override
            public TextView getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView = createCustomTextView(position);
                if (position == 0) {
                    textView.setHeight(0);
                    return textView;
                }

                final float scaledDensityRatio = Dimensions.getScaledDensityRatio(context);

                textView.setPadding(
                    Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
                    Dimensions.toPixel(Dimensions.inputPaddingTopBottom * scaledDensityRatio),
                    Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
                    Dimensions.toPixel(Dimensions.inputPaddingTopBottom * scaledDensityRatio)
                );

                // Make a group item to look like a disabled label
                if (Objects.equals(flattenedOptions.get(position).getType(), "group")) {
                    textView.setTextColor(Color.parseColor(Colors.disabledItem));
                    textView.setTextSize(Dimensions.textSizeSmall);
                    textView.setTypeface(null, Typeface.BOLD);
                }

                if (position == dropdownGroup.getSelectedItemPosition()) {
                    textView.setBackground(Drawables.getSelectedDropdownItemDrawable());
                }

                return textView;
            }

            private TextView createCustomTextView(int position) {
                TextView selectedOption = new TextView(context);
                selectedOption.setLayoutParams(
                    new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                );
                final float scaledDensityRatio = Dimensions.getScaledDensityRatio(context);
                selectedOption.setPadding(
                    Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
                    Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
                    Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
                    Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio)
                );

                selectedOption.setTextSize(Dimensions.textSizeMedium);
                selectedOption.setText(Objects.requireNonNull(getItem(position)).getLabel());

                return selectedOption;
            }
        };

        dropdownGroup.setAdapter(adapter);
        setDefaultValue(flattenedOptions);

        dropdownGroup.setOnTouchListener((v, event) -> {
            dropdownGroup.requestFocusFromTouch();
            dropdownGroup.performClick();
            // Note: return false to allow the Spinner to open
            return false;
        });

        setView(dropdownGroup);
    }

    private View getSelectView(Function<Integer, TextView> createCustomTextView, int position) {
        RelativeLayout parentLayoutView = new RelativeLayout(context);
        parentLayoutView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );

        // Select label
        TextView fieldLabel = createCustomTextView.apply(0);

        final float scaledDensityRatio = Dimensions.getScaledDensityRatio(context);

        fieldLabel.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        fieldLabel.setPadding(
            Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPaddingVertical * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPaddingVertical * scaledDensityRatio)
        );

        // Add the selected option value
        TextView selectedOption = createCustomTextView.apply(position);

        selectedOption.setPadding(
            Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPaddingTop * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.dropdownSelectionRightPadding * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio)
        );

        // Note: this will not be null because a placeholder always selected
        Option selectedItem = (Option) dropdownGroup.getSelectedItem();

        // Position the selected item label and value
        if (selectedItem.getValue() != null) {
            fieldLabel.setTranslationY(
                fieldLabel.getTranslationY() - Dimensions.toPixel(Dimensions.inputFocusTranslateY)
            );
            fieldLabel.setTextSize(Dimensions.textSizeSmall);
        } else {
            selectedOption.setVisibility(View.GONE);
        }

        parentLayoutView.addView(fieldLabel);
        parentLayoutView.addView(selectedOption);

        // Add buttons layout
        LinearLayout buttonsLayout = new LinearLayout(context);
        RelativeLayout.LayoutParams buttonsLayoutParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        buttonsLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        buttonsLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        buttonsLayoutParams.setMargins(
            0,
            Dimensions.toPixel(Dimensions.inputPaddingTopBottom * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPadding * scaledDensityRatio),
            Dimensions.toPixel(Dimensions.inputPaddingTopBottom * scaledDensityRatio)
        );
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLayout.setLayoutParams(buttonsLayoutParams);

        // Add cancel button if not the default select label option is selected
        if (position != 0) {
            ImageView clearButton = new ImageView(context);
            clearButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                Dimensions.toPixel(Dimensions.buttonMedium),
                Dimensions.toPixel(Dimensions.buttonMedium)
            );
            layoutParams.setMargins(
                Dimensions.toPixel(Dimensions.paddingSmall * scaledDensityRatio),
                0,
                Dimensions.toPixel(Dimensions.paddingSmall * scaledDensityRatio),
                0
            );
            clearButton.setLayoutParams(layoutParams);
            clearButton.setOnClickListener(view -> dropdownGroup.setSelection(0));
            buttonsLayout.addView(clearButton);
        }

        // Create arrow down button
        ImageView arrowButton = new ImageView(context);
        arrowButton.setImageResource(android.R.drawable.arrow_down_float);
        arrowButton.setImageTintList(ColorStateList.valueOf(Color.parseColor(Colors.primary)));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
            Dimensions.toPixel(Dimensions.buttonMedium),
            Dimensions.toPixel(Dimensions.buttonMedium)
        );
        layoutParams.setMargins(
            Dimensions.toPixel(Dimensions.paddingSmall * scaledDensityRatio),
            0,
            Dimensions.toPixel(Dimensions.paddingSmall * scaledDensityRatio),
            0
        );
        arrowButton.setLayoutParams(layoutParams);

        buttonsLayout.addView(arrowButton);

        parentLayoutView.addView(buttonsLayout);

        return parentLayoutView;
    }

    private void setDefaultValue(List<Option> flattenedOptions) {
        String initValue = widgetModel.getValue();
        if (initValue == null) {
            // Note: always have a selected item to avoid null
            dropdownGroup.setSelection(0);
            return;
        }

        for (int i = 0; i < flattenedOptions.size(); i++) {
            if (Objects.equals(flattenedOptions.get(i).getValue(), initValue)) {
                dropdownGroup.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void clearError() {
        super.clearError();
        dropdownGroup.setBackground(Drawables.focusableInputBackgroundDrawable());
    }

    @Override
    public void showError(String message) {
        super.showError(message);
        this.dropdownGroup.setBackground(Drawables.getInputErrorDrawable());
    }

    @Override
    public String getValue() {
        Option selectedOption = (Option) dropdownGroup.getSelectedItem();
        return selectedOption.getValue();
    }
}
