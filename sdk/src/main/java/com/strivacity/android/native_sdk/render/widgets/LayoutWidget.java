package com.strivacity.android.native_sdk.render.widgets;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.strivacity.android.native_sdk.render.Form;
import com.strivacity.android.native_sdk.render.ViewFactory;
import com.strivacity.android.native_sdk.render.models.BrandingModel;
import com.strivacity.android.native_sdk.render.models.LayoutModel;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class LayoutWidget extends Widget {

    private final List<Widget> items;

    public LayoutWidget(
        ViewFactory viewFactory,
        BrandingModel brandingModel,
        Map<String, Form> forms,
        LayoutModel.SingleLayoutModel singleLayoutModel
    ) {
        super(viewFactory.getContext());
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        setView(linearLayout);

        switch (singleLayoutModel.getType()) {
            case "horizontal":
                this.<LinearLayout>typedView().setOrientation(LinearLayout.HORIZONTAL);
                break;
            case "vertical":
                this.<LinearLayout>typedView().setOrientation(LinearLayout.VERTICAL);
                break;
            default:
                throw new RuntimeException();
        }

        items =
            singleLayoutModel
                .getItems()
                .stream()
                .map(layoutModel -> {
                    if (layoutModel instanceof LayoutModel.WidgetReferenceModel) {
                        LayoutModel.WidgetReferenceModel widgetReferenceModel = (LayoutModel.WidgetReferenceModel) layoutModel;
                        return forms
                            .get(widgetReferenceModel.getFormId())
                            .getWidgets()
                            .get(widgetReferenceModel.getWidgetId());
                    }

                    if (layoutModel instanceof LayoutModel.SingleLayoutModel) {
                        return viewFactory.layoutWidget(
                            forms,
                            brandingModel,
                            (LayoutModel.SingleLayoutModel) layoutModel
                        );
                    }

                    throw new RuntimeException();
                })
                .collect(Collectors.toList());

        ViewGroup viewGroup = typedView();
        items.forEach(item -> viewGroup.addView(item.getView()));
    }
}
