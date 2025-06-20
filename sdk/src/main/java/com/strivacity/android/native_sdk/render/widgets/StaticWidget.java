package com.strivacity.android.native_sdk.render.widgets;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.strivacity.android.native_sdk.render.models.WidgetModel;

public class StaticWidget extends Widget {

    private final String NOT_VALID_RENDER_TYPE = "Not a valid render type";
    protected final TextView textView;

    public StaticWidget(Context context, WidgetModel.StaticWidgetModel widgetModel) {
        super(context);
        textView = new TextView(context);

        if ("html".equals(widgetModel.getRender().getType())) {
            textView.setText(Html.fromHtml(widgetModel.getValue(), Html.FROM_HTML_MODE_COMPACT));
        } else if ("text".equals(widgetModel.getRender().getType())) {
            textView.setText(widgetModel.getValue());
        } else {
            throw new RuntimeException(NOT_VALID_RENDER_TYPE);
        }
        textView.setHorizontallyScrolling(false);
        setView(textView);
    }
}
