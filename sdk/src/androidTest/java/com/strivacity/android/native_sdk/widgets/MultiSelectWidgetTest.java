package com.strivacity.android.native_sdk.widgets;

import com.strivacity.android.native_sdk.util.JSON;
import com.strivacity.android.native_sdk.utils.LoadJsonFromResource;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MultiSelectWidgetTest {

    @Test
    public void readGroupMultiSelectMock() throws Exception {
        // Load the JSON file from resources
        JSON json = LoadJsonFromResource.loadJsonFromResource("mocks/widgets/multiselect/MultiSelectMock.json");

        Assert.assertEquals("multiSelect", json.string("type"));
        Assert.assertEquals("Mandatory methods", json.string("label"));
        Assert.assertEquals("mandatory", json.string("id"));
        Assert.assertEquals(false, json.bool("readonly"));

        List<String> actualArray = json.stringList("value");
        List<String> expectedArray = List.of("email", "phone");

        Assert.assertEquals(expectedArray, actualArray);

        List<JSON> options = json.list("options");
        Assert.assertEquals(2, options.size());
        Assert.assertEquals("group", options.get(0).string("type"));
        Assert.assertEquals("Own methods", options.get(0).string("label"));

        Assert.assertEquals("group", options.get(1).string("type"));
        Assert.assertEquals("Third party methods", options.get(1).string("label"));
    }
}
