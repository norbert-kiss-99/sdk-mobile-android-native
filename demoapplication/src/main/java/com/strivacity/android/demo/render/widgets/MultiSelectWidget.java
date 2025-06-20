package com.strivacity.android.demo.render.widgets;

import static com.strivacity.android.demo.render.constants.Dimensions.getScaledDensityRatio;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.strivacity.android.demo.render.constants.Colors;
import com.strivacity.android.demo.render.constants.Dimensions;
import com.strivacity.android.demo.render.constants.Drawables;
import com.strivacity.android.native_sdk.render.models.WidgetModel.MultiSelectWidgetModel;

public class MultiSelectWidget extends com.strivacity.android.native_sdk.render.widgets.MultiSelectWidget {

    public MultiSelectWidget(Context context, MultiSelectWidgetModel widgetModel) {
        super(context, widgetModel);
        final float scaledDensityRatio = getScaledDensityRatio(context);

        // Creating the parent layout

        LinearLayout parentLayoutView = this.typedView();
        parentLayoutView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        );
        parentLayoutView.setOrientation(LinearLayout.VERTICAL);

        // Modify multi select label

        multiSelectLabel.setTextSize(Dimensions.textSizeMedium);

        // Add styled options to the multiselect

        checkBoxContainers.forEach(container -> {
            formatCheckboxLayout(container.getLayout());
            CheckBox checkBox = container.getCheckbox();

            container
                .getLayout()
                .setPadding(
                    Dimensions.toPixel(Dimensions.paddingMedium * scaledDensityRatio),
                    Dimensions.toPixel(Dimensions.paddingMedium * scaledDensityRatio),
                    0,
                    Dimensions.toPixel(Dimensions.paddingMedium * scaledDensityRatio)
                );
            //Checkbox
            checkBox.setButtonTintList(Drawables.checkboxDrawable());
            checkBox.setTextSize(Dimensions.textSizeMedium);
            checkBox.setPadding(Dimensions.toPixel(Dimensions.paddingSmall), 0, 0, 0);

            if (checkBox.isChecked()) {
                checkBox.setTextColor(Color.parseColor(Colors.primary));
                container.getLayout().setBackground(Drawables.multiSelectCheckboxDrawable());
            }
        });

        groupTitleContainers.forEach(container -> {
            formatCheckboxLayout(container.getLayout());

            container
                .getLayout()
                .setPadding(0, Dimensions.toPixel(Dimensions.paddingMedium * scaledDensityRatio), 0, 0);

            container.getGroupTitle().setTextSize(Dimensions.textSizeMedium);
            container.getGroupTitle().setTextColor(Color.parseColor(Colors.disabledItem));
        });

        // Modify error label to the view
        errorLabelView.setPadding(0, 0, 0, Dimensions.toPixel(Dimensions.paddingSmall));
        errorLabelView.setTextSize(Dimensions.textSizeSmall);
    }

    private static void formatCheckboxLayout(LinearLayout checkboxLayoutView) {
        // Checkbox layout
        checkboxLayoutView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );
        layoutParams.setMargins(
            0,
            Dimensions.toPixel(Dimensions.paddingMedium),
            0,
            Dimensions.toPixel(Dimensions.paddingMedium)
        );
        checkboxLayoutView.setLayoutParams(layoutParams);
    }

    @Override
    protected void checkedChangeListener(CheckBox checkbox, LinearLayout layout, String value) {
        super.checkedChangeListener(checkbox, layout, value);
        if (checkbox.isChecked()) {
            checkbox.setTextColor(Color.parseColor(Colors.primary));
            layout.setBackground(Drawables.multiSelectCheckboxDrawable());
        } else {
            checkbox.setTextColor(Color.parseColor(Colors.textColor));
            layout.setBackground(null);
        }
    }
}
