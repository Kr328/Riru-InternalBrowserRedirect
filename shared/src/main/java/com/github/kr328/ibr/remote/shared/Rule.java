package com.github.kr328.ibr.remote.shared;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Rule implements Parcelable {
    public static final Creator<Rule> CREATOR = new Creator<Rule>() {
        @Override
        public Rule createFromParcel(Parcel in) {
            return new Rule(in);
        }

        @Override
        public Rule[] newArray(int size) {
            return new Rule[size];
        }
    };
    public String tag;
    public Uri urlPath;
    public String regexIgnore;
    public String regexForce;

    public Rule() {
    }

    private Rule(Parcel in) {
        tag = in.readString();
        urlPath = Uri.parse(in.readString());
        regexIgnore = in.readString();
        regexForce = in.readString();

        tag = tag == null ? "" : tag;
        regexIgnore = regexIgnore == null ? "" : regexIgnore;
        regexForce = regexForce == null ? "" : regexForce;
    }

    static Rule parseFromJson(JSONObject jsonObject) throws JSONException {
        Rule result = new Rule();

        result.tag = jsonObject.getString("tag");
        result.urlPath = Uri.parse(jsonObject.getString("url-path"));
        result.regexForce = jsonObject.optString("regex-force", "");
        result.regexIgnore = jsonObject.optString("regex-ignore", "");

        if (result.urlPath == Uri.EMPTY)
            throw new JSONException("Invalid url path");

        return result;
    }

    @Override
    public int describeContents() {
        return "Rule".hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tag);
        parcel.writeString(urlPath.toString());
        parcel.writeString(regexIgnore);
        parcel.writeString(regexForce);
    }

    JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();

        result.put("tag", tag);
        result.put("url-path", urlPath.toString());
        result.put("regex-force", regexForce);
        result.put("regex-ignore", regexIgnore);

        return result;
    }
}
