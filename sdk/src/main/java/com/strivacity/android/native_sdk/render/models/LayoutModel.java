package com.strivacity.android.native_sdk.render.models;

import com.strivacity.android.native_sdk.util.JSON;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class LayoutModel {

    private final String type;

    @Getter
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class SingleLayoutModel extends LayoutModel {

        private final List<LayoutModel> items;

        public SingleLayoutModel(JSON json) {
            super(json.string("type"));
            items =
                json
                    .list("items")
                    .stream()
                    .map(jsonItem -> {
                        String type = jsonItem.string("type");
                        switch (type) {
                            case "widget":
                                return new WidgetReferenceModel(jsonItem);
                            case "horizontal":
                            case "vertical":
                                return new SingleLayoutModel(jsonItem);
                            default:
                                throw new RuntimeException("Unknown layout type: " + type);
                        }
                    })
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class WidgetReferenceModel extends LayoutModel {

        private final String formId;
        private final String widgetId;

        public WidgetReferenceModel(JSON json) {
            super(json.string("type"));
            this.formId = json.string("formId");
            this.widgetId = json.string("widgetId");
        }
    }
}
