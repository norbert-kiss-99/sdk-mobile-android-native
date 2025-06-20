package com.strivacity.android.demo.render.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.widget.CheckBox;
import android.widget.TextView;

import com.strivacity.android.demo.render.constants.Colors;
import com.strivacity.android.demo.render.constants.Dimensions;
import com.strivacity.android.native_sdk.render.models.WidgetModel;

public class CheckboxWidget extends com.strivacity.android.native_sdk.render.widgets.CheckboxWidget {

    public CheckboxWidget(Context context, WidgetModel.CheckboxWidgetModel widgetModel) {
        super(context, widgetModel);
        switch (widgetModel.getRender().getType()) {
            case "checkboxShown":
                CheckBox designedCheckboxView = (CheckBox) checkboxView;
                designedCheckboxView.setPadding(Dimensions.toPixel(Dimensions.paddingMedium), 0, 0, 0);
                designedCheckboxView.setTextSize(Dimensions.textSizeMedium);
                designedCheckboxView.setButtonTintList(ColorStateList.valueOf(Color.parseColor(Colors.primary)));
                designedCheckboxView.setLinkTextColor(Color.parseColor(Colors.primary));
                designedCheckboxView.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case "checkboxHidden":
                TextView textView = checkboxView;
                textView.setTextSize(Dimensions.textSizeMedium);
                textView.setLinkTextColor(Color.parseColor(Colors.primary));
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                break;
        }

        // Modify the error label below the input

        errorLabelView.setPadding(0, 0, 0, Dimensions.toPixel(Dimensions.paddingSmall));
        errorLabelView.setTextSize(Dimensions.textSizeSmall);
        errorLabelView.setTextColor(Color.parseColor(Colors.danger));
    }
}
