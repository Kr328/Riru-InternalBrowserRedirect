package com.github.kr328.ibr.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Rule {
    private String tag;
    private String urlPath;
    private Pattern regexIgnore;
    private Pattern regexForce;

    public static Rule parseFromJson(JSONObject jsonObject) throws JSONException {
        Rule result = new Rule();

        result.tag = jsonObject.getString("tag");
        result.urlPath = jsonObject.getString("url-path");
        result.regexForce = Pattern.compile(jsonObject.optString("regex-force", ""));
        result.regexIgnore = Pattern.compile(jsonObject.optString("regex-ignore", ""));

        return result;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();

        result.put("tag", tag);
        result.put("url-path", urlPath);
        result.put("regex-force", regexForce);
        result.put("regex-ignore", regexIgnore);

        return result;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public Pattern getRegexIgnore() {
        return regexIgnore;
    }

    public void setRegexIgnore(Pattern regexIgnore) {
        this.regexIgnore = regexIgnore;
    }

    public Pattern getRegexForce() {
        return regexForce;
    }

    public void setRegexForce(Pattern regexForce) {
        this.regexForce = regexForce;
    }
}
