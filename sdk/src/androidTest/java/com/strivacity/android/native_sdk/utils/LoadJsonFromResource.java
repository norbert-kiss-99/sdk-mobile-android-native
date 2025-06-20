package com.strivacity.android.native_sdk.utils;

import com.strivacity.android.native_sdk.util.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class LoadJsonFromResource {

    /*  Example to use in a widget:
        JSON json = LoadJsonFromResource.loadJsonFromResource("MultiSelectMock.json");
        widgetModel = new MultiSelectWidgetModel(json);
    */
    public static JSON loadJsonFromResource(String filePath) throws IOException, JSONException {
        InputStream inputStream = LoadJsonFromResource.class.getClassLoader().getResourceAsStream(filePath);

        // Read the input stream
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        inputStream.close();

        // Convert the file content to a JSON string
        String jsonString = new String(buffer, StandardCharsets.UTF_8);

        return new JSON(new JSONObject(jsonString));
    }
}
