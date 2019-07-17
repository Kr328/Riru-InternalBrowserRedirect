package com.github.kr328.ibr.model;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Rule {
    private String tag;
    private Uri urlPath;
    private Pattern regexIgnore;
    private Pattern regexForce;

    public static Rule parseFromJson(JSONObject jsonObject) throws JSONException {
        Rule result = new Rule();

        result.tag = jsonObject.getString("tag");
        result.urlPath = Uri.parse(jsonObject.getString("url-path"));
        result.regexForce = Pattern.compile(jsonObject.optString("regex-force", ""));
        result.regexIgnore = Pattern.compile(jsonObject.optString("regex-ignore", ""));

        if ( result.urlPath.equals(Uri.EMPTY) )
            throw new JSONException("Invalid url path");

        return result;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();

        result.put("tag", tag);
        result.put("url-path", urlPath.toString());
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

    public Uri getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(Uri urlPath) {
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
