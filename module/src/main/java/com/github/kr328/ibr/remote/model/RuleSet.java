package com.github.kr328.ibr.remote.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RuleSet implements Parcelable {
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
    public String tag;
    public List<String> extras;
    public List<Rule> rules;
    public boolean debug;

    private RuleSet() {
        tag = "";
        extras = new ArrayList<>();
        rules = new ArrayList<>();
        debug = false;
    }

    private RuleSet(Parcel in) {
        tag = in.readString();
        extras = new ArrayList<>();
        in.readStringList(extras);
        rules = in.createTypedArrayList(Rule.CREATOR);
        debug = in.readBoolean();
    }

    public static RuleSet readFromJson(JSONObject jsonObject) throws JSONException {
        RuleSet result = new RuleSet();

        result.tag = jsonObject.getString("tag");
        result.debug = jsonObject.optBoolean("debug", false);

        JSONArray array = jsonObject.getJSONArray("extras");
        for (int i = 0; i < array.length(); i++)
            result.extras.add(array.getString(i));

        array = jsonObject.getJSONArray("rules");
        for (int i = 0; i < array.length(); i++)
            result.rules.add(Rule.parseFromJson(array.getJSONObject(i)));

        return result;
    }

    @Override
    public int describeContents() {
        return "RuleSet".hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tag);
        parcel.writeStringList(extras);
        parcel.writeTypedList(rules);
        parcel.writeBoolean(debug);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();

        JSONArray array = new JSONArray();
        for (Rule rule : rules)
            array.put(rule.toJson());

        result.put("tag", tag);
        result.put("rules", array);
        result.put("extras", new JSONArray(extras));
        result.put("debug", debug);

        return result;
    }
}
