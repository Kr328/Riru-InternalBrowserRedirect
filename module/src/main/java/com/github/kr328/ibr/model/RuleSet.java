package com.github.kr328.ibr.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RuleSet {
    private String tag;
    private List<Rule> rules;

    public static RuleSet readFromJson(JSONObject jsonObject) throws JSONException {
        RuleSet result = new RuleSet();

        result.tag = jsonObject.getString("tag");
        result.rules = new ArrayList<>();

        JSONArray array = jsonObject.getJSONArray("rules");
        for ( int i = 0 ; i < array.length() ; i++ )
            result.rules.add(Rule.parseFromJson(array.getJSONObject(i)));

        return result;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();

        JSONArray array = new JSONArray();
        for ( Rule rule : rules )
            array.put(rule.toJson());

        result.put("tag", tag);
        result.put("rules", array);

        return result;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }
}
