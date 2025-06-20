package com.strivacity.android.native_sdk.widgets;

import com.strivacity.android.native_sdk.util.JSON;
import com.strivacity.android.native_sdk.utils.LoadJsonFromResource;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SelectWidgetTest {

    @Test
    public void readGroupDropdownSelectMock() throws Exception {
        // Load the JSON file from resources
        JSON json = LoadJsonFromResource.loadJsonFromResource("mocks/widgets/select/GroupDropdownSelectMock.json");

        Assert.assertEquals("select", json.string("type"));
        Assert.assertEquals("Country", json.string("label"));
        Assert.assertEquals("attributes.country", json.string("id"));
        Assert.assertEquals(false, json.bool("readonly"));
        Assert.assertEquals("FI", json.string("value"));

        JSON render = json.object("render");
        Assert.assertEquals("dropdown", render.string("type"));

        List<JSON> options = json.list("options");
        Assert.assertEquals(2, options.size());
        Assert.assertEquals("group", options.get(0).string("type"));
        Assert.assertEquals("Europe", options.get(0).string("label"));

        Assert.assertEquals("group", options.get(1).string("type"));
        Assert.assertEquals("USA", options.get(1).string("label"));
    }
}
