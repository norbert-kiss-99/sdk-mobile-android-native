package com.strivacity.android.demo.render.widgets.select.simple;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;

import com.strivacity.android.demo.render.constants.Colors;
import com.strivacity.android.demo.render.constants.Dimensions;
import com.strivacity.android.demo.render.constants.Drawables;
import com.strivacity.android.native_sdk.render.models.WidgetModel.SelectWidgetModel;

public class RadioWidget extends com.strivacity.android.native_sdk.render.widgets.select.simple.RadioWidget {

    public RadioWidget(Context context, SelectWidgetModel widgetModel) {
        super(context, widgetModel);
        ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(Colors.primary));

        //Set the label of the select
        selectLabel.setTextSize(Dimensions.textSizeMedium);
        selectLabel.setText(widgetModel.getLabel());

        radioOptionList.forEach(option -> {
            option.getRadioButton().setButtonTintList(colorStateList);
            option.getRadioButton().setTextSize(Dimensions.textSizeMedium);
            option.getRadioButton().setPadding(Dimensions.toPixel(Dimensions.paddingMedium), 0, 0, 0);
        });

        groupTitleList.forEach(title -> {
            title.setTextSize(Dimensions.textSizeSmall);
            title.setTextColor(Color.parseColor(Colors.disabledItem));
            title.setPadding(
                0,
                Dimensions.toPixel(Dimensions.paddingSmall),
                0,
                Dimensions.toPixel(Dimensions.paddingMedium)
            );
        });
    }

    @Override
    public void clearError() {
        super.clearError();
        radioGroup.setBackground(Drawables.getTransparentBackgroundDrawable());
    }

    @Override
    public void showError(String message) {
        super.showError(message);
        this.radioGroup.setBackground(Drawables.getInputErrorDrawable());
    }
}
