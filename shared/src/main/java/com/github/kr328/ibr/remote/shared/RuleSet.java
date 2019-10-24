package com.github.kr328.ibr.remote.shared;

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

    public RuleSet() {
        tag = "";
        extras = new ArrayList<>();
        rules = new ArrayList<>();
        debug = false;
    }

    private RuleSet(Parcel in) {
        tag = in.readString();
        in.readStringList(extras = new ArrayList<>());
        rules = in.createTypedArrayList(Rule.CREATOR);
        debug = in.readInt() == 1;
    }

    public static RuleSet readFromJson(JSONObject jsonObject) throws JSONException {
        RuleSet result = new RuleSet();

        result.tag = jsonObject.getString("tag");
        result.debug = jsonObject.optBoolean("debug", false);

        JSONArray array;

        array = jsonObject.getJSONArray("extras");
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
        parcel.writeInt(debug ? 1 : 0);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();

        JSONArray ruleArray = new JSONArray();
        for (Rule rule : rules)
            ruleArray.put(rule.toJson());

        JSONArray extrasArray = new JSONArray();
        for ( String extra : extras )
            extrasArray.put(extra);

        result.put("tag", tag);
        result.put("extras", extrasArray);
        result.put("rules", ruleArray);
        result.put("debug", debug);

        return result;
    }
}
