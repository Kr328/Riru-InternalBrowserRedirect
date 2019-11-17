package com.github.kr328.ibr.remote.server;

import android.util.Log;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.shared.RuleSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

class StoreManager {
    private final static StoreManager INSTANCE = new StoreManager();
    private HashMap<String, RuleSet> ruleSets = new HashMap<>();

    private StoreManager() {
    }

    static StoreManager getInstance() {
        return INSTANCE;
    }

    synchronized RuleSet getRuleSet(String pkg) {
        return ruleSets.get(pkg);
    }

    synchronized Map<String, RuleSet> getRuleSets() {
        return ruleSets;
    }

    synchronized void updateRuleSet(String pkg, RuleSet ruleSet) {
        ruleSets.put(pkg, ruleSet);
        try {
            FileUtils.writeLines(new File(Constants.DATA_STORE_DIRECTORY, String.format(Constants.TEMPLATE_CONFIG_FILE_NAME, pkg)), ruleSet.toJson().toString());
        } catch (Exception e) {
            Log.d(Constants.TAG, "Save data failure pkg = " + pkg, e);
        }
    }

    synchronized void removeRuleSet(String pkg) {
        ruleSets.remove(pkg);
        //noinspection ResultOfMethodCallIgnored
        new File(Constants.DATA_STORE_DIRECTORY, String.format(Constants.TEMPLATE_CONFIG_FILE_NAME, pkg)).delete();
    }

    synchronized void load() {
        ruleSets = new HashMap<>();

        File[] files = new File(Constants.DATA_STORE_DIRECTORY).listFiles();
        if (files == null)
            return;

        for (File file : files) {
            Matcher matcher = Constants.PATTERN_CONFIG_FILE.matcher(file.getName());
            if (matcher.matches()) {
                try {
                    ruleSets.put(matcher.group(1), RuleSet.readFromJson(new JSONObject(FileUtils.readLines(file))));
                } catch (IOException | JSONException e) {
                    Log.w(Constants.TAG, "Load " + file.toString() + " failure", e);
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                }
            }
        }

        Log.i(Constants.TAG, "Loaded RuleSet " + ruleSets.keySet());
    }
}
