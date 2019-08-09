package com.github.kr328.ibr.remote.model;

import org.json.JSONException;
import org.json.JSONObject;

public class General {
    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    private boolean debugMode = false;

    public static General parseFromJson(JSONObject jsonObject) {
        General result = new General();

        result.debugMode = jsonObject.optBoolean("debugMode", false);

        return result;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();

        result.put("debugMode", debugMode);

        return result;
    }
}
