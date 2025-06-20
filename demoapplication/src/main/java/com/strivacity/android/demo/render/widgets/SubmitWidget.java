package com.strivacity.android.demo.render.widgets;

import static com.strivacity.android.demo.render.constants.Dimensions.toPixel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strivacity.android.demo.render.constants.Colors;
import com.strivacity.android.demo.render.constants.Dimensions;
import com.strivacity.android.native_sdk.render.models.WidgetModel;

import java.util.Objects;

public class SubmitWidget extends com.strivacity.android.native_sdk.render.widgets.SubmitWidget {

    public SubmitWidget(Context context, WidgetModel.SubmitWidgetModel submitWidgetModel) {
        super(context, submitWidgetModel);
        switch (submitWidgetModel.getRender().getType()) {
            case "button":
                Button button = this.typedView();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(0, 0, 0, Dimensions.toPixel(Dimensions.spaceBetweenButtons));
                button.setLayoutParams(params);

                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setCornerRadius(toPixel(Dimensions.borderRadius));

                String backgroundColor = submitWidgetModel.getRender().getBgColor();
                String backgroundByVariant;
                String textByVariant;
                if (Objects.equals(submitWidgetModel.getRender().getHint().getVariant(), "primary")) {
                    backgroundByVariant = Colors.primary;
                    textByVariant = Colors.white;
                } else {
                    backgroundByVariant = Colors.white;
                    textByVariant = Colors.primary;
                }

                shape.setColor(
                    (
                        backgroundColor == null
                            ? Color.parseColor(backgroundByVariant)
                            : Color.parseColor(backgroundColor)
                    )
                );
                button.setBackground(shape);
                String textColor = submitWidgetModel.getRender().getTextColor();
                button.setTextColor(textColor == null ? Color.parseColor(textByVariant) : Color.parseColor(textColor));
                button.setTextSize(Dimensions.textSizeMedium);
                button.setAllCaps(false);
                break;
            case "link":
                TextView textView = this.typedView();
                textView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                );

                textView.setPadding(
                    toPixel(Dimensions.paddingMedium),
                    toPixel(Dimensions.paddingMedium),
                    toPixel(Dimensions.paddingMedium),
                    toPixel(Dimensions.paddingMedium)
                );
                textView.setTextSize(Dimensions.textSizeMedium);
                textView.setTextColor(Color.parseColor(Colors.primary));
                break;
        }
    }
}
