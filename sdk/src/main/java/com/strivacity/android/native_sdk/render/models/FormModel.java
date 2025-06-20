package com.strivacity.android.native_sdk.render.models;

import com.strivacity.android.native_sdk.util.JSON;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@ToString
public class FormModel {

    private final String id;
    private final String type;
    private final List<WidgetModel> widgets;

    public FormModel(JSON json) {
        this.id = json.string("id");
        this.type = json.string("type");

        widgets = json.list("widgets").stream().map(WidgetModel::fromJson).collect(Collectors.toList());
    }
}
