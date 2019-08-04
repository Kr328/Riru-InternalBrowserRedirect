package com.github.kr328.ibr.remote.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RuleSet implements Parcelable {
    private String tag;
    private String token;
    private List<Rule> rules;

    public RuleSet() {
        tag = "";
        token = "";
        rules = new ArrayList<>();
    }

    private RuleSet(Parcel in) {
        tag = in.readString();
        token = in.readString();
        rules = in.createTypedArrayList(com.github.kr328.ibr.remote.model.Rule.CREATOR);
    }

    public static final Creator<RuleSet> CREATOR = new Creator<RuleSet>() {
        @Override
        public RuleSet createFromParcel(Parcel in) {
            return new RuleSet(in);
        }

        @Override
        public RuleSet[] newArray(int size) {
            return new RuleSet[size];
        }
    };

    @Override
    public int describeContents() {
        return "RuleSet".hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tag);
        parcel.writeString(token);
        parcel.writeTypedList(rules);
    }

    public static RuleSet readFromJson(JSONObject jsonObject) throws JSONException {
        RuleSet result = new RuleSet();

        result.tag = jsonObject.getString("tag");
        result.token = jsonObject.getString("token");

        JSONArray array = jsonObject.getJSONArray("rules");
        for ( int i = 0 ; i < array.length() ; i++ )
            result.rules.add(com.github.kr328.ibr.remote.model.Rule.parseFromJson(array.getJSONObject(i)));

        return result;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();

        JSONArray array = new JSONArray();
        for ( com.github.kr328.ibr.remote.model.Rule rule : rules )
            array.put(rule.toJson());

        result.put("tag", tag);
        result.put("rules", array);
        result.put("token", token);

        return result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<com.github.kr328.ibr.remote.model.Rule> getRules() {
        return rules;
    }

    public void setRules(List<com.github.kr328.ibr.remote.model.Rule> rules) {
        this.rules = rules;
    }
}
