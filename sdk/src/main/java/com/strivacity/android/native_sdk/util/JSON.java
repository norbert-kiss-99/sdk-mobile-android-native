package com.strivacity.android.native_sdk.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class JSON {

    private final JSONObject jsonObject;

    public List<String> keys() {
        ArrayList<String> keys = new ArrayList<>();
        jsonObject.keys().forEachRemaining(keys::add);
        return keys;
    }

    public String string(String key) {
        try {
            return parseString(jsonObject, key);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer integer(String key) {
        try {
            return parseInt(jsonObject, key);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean bool(String key) {
        try {
            return parseBoolean(jsonObject, key);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> stringList(String key) {
        try {
            return parseStringList(jsonObject, key);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public JSON object(String key) {
        if (jsonObject.isNull(key)) {
            return null;
        }

        try {
            return new JSON(jsonObject.getJSONObject(key));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public List<JSON> list(String key) {
        try {
            List<JSON> response = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray(key);

            for (int i = 0; i < jsonArray.length(); i++) {
                response.add(new JSON(jsonArray.getJSONObject(i)));
            }

            return response;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isNull(String key) {
        return jsonObject.isNull(key);
    }

    public static String parseString(JSONObject jsonObject, String key) throws JSONException {
        return jsonObject.isNull(key) ? null : jsonObject.getString(key);
    }

    public static Integer parseInt(JSONObject jsonObject, String key) throws JSONException {
        return jsonObject.isNull(key) ? null : jsonObject.getInt(key);
    }

    public static boolean parseBoolean(JSONObject jsonObject, String key) throws JSONException {
        return !jsonObject.isNull(key) && jsonObject.getBoolean(key);
    }

    public static List<String> parseStringList(JSONObject jsonObject, String key) throws JSONException {
        List<String> stringList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray(key);

        for (int i = 0; i < jsonArray.length(); i++) {
            stringList.add(jsonArray.getString(i));
        }

        return stringList;
    }

    public static void put(JSONObject jsonObject, String key, Object value) throws JSONException {
        String[] parts = key.split("\\.");

        for (int i = 0; i < parts.length; ++i) {
            if (i == parts.length - 1) {
                jsonObject.put(parts[i], resolve(value));
            } else {
                if (jsonObject.isNull(parts[i])) {
                    jsonObject.put(parts[i], new JSONObject());
                }

                jsonObject = jsonObject.getJSONObject(parts[i]);
            }
        }
    }

    private static Object resolve(Object value) {
        if (value == null) {
            return null;
        }

        if (List.class.isAssignableFrom(value.getClass())) {
            JSONArray array = new JSONArray();
            List list = (List) value;
            list.forEach(item -> array.put(resolve(item)));
            return array;
        } else if (Set.class.isAssignableFrom(value.getClass())) {
            JSONArray array = new JSONArray();
            Set set = (Set) value;
            set.forEach(item -> array.put(item));
            return array;
        } else if (Map.class.isAssignableFrom(value.getClass())) {
            JSONObject object = new JSONObject();
            Map<String, Object> valueMap = (Map<String, Object>) value;
            valueMap.forEach((entryKey, entryValue) -> {
                try {
                    put(object, entryKey, entryValue);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            return object;
        } else {
            return value;
        }
    }
}
