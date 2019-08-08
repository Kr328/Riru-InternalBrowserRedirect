package com.github.kr328.ibr.remote.data;

import android.util.Log;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.FileUtils;
import com.github.kr328.ibr.remote.model.General;
import com.github.kr328.ibr.remote.model.RuleSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;

public class StoreManager {
    public static StoreManager getInstance() {
        return INSTANCE;
    }

    public synchronized RuleSet getRuleSet(String pkg) {
        return cache.ruleSets.get(pkg);
    }

    public synchronized Map<String, RuleSet> getRuleSets() {
        return cache.ruleSets;
    }

    public synchronized void setDebugModeEnabled(boolean enabled) {
        cache.general.setDebugMode(enabled);
        background.execute(() -> {
            try {
                FileUtils.writeLines(new File(Constants.DATA_STORE_DIRECTORY, "general.json"), cache.general.toJson().toString());
            } catch (JSONException | IOException e) {
                Log.e(Constants.TAG, "Save general failure");
            }
        });
    }

    public synchronized boolean isDebugModeEnabled() {
        return cache.general.isDebugMode();
    }

    public synchronized void updateRuleSet(String pkg, RuleSet ruleSet) {
        cache.ruleSets.put(pkg, ruleSet);
        background.execute(() -> {
            try {
                FileUtils.writeLines(new File(Constants.DATA_STORE_DIRECTORY, String.format(Constants.TEMPLATE_CONFIG_FILE, pkg)), ruleSet.toJson().toString());
            } catch (Exception e) {
                Log.d(Constants.TAG, "Save data failure pkg = " + pkg, e);
            }
        });
    }

    public synchronized void removeRuleSet(String pkg) {
        cache.ruleSets.remove(pkg);
        background.execute(() -> {
            //noinspection ResultOfMethodCallIgnored
            new File(Constants.DATA_STORE_DIRECTORY, String.format(Constants.TEMPLATE_CONFIG_FILE, pkg)).delete();
        });
    }

    private synchronized void load() {
        cache.ruleSets = new HashMap<>();

        try {
            cache.general = General.parseFromJson(new JSONObject(FileUtils.readLines(Constants.DATA_STORE_DIRECTORY + "/general.json")));
        } catch (IOException | JSONException e) {
            Log.w(Constants.TAG, "Load general config failure", e);
            cache.general = new General();
        }

        File[] files = new File(Constants.DATA_STORE_DIRECTORY).listFiles();
        if (files == null)
            return;

        for (File f : files) {
            Matcher matcher = Constants.PATTERN_CONFIG_FILE.matcher(f.getName());
            if (matcher.matches()) {
                try {
                    cache.ruleSets.put(matcher.group(1), RuleSet.readFromJson(new JSONObject(FileUtils.readLines(f))));
                } catch (IOException | JSONException e) {
                    Log.w(Constants.TAG, "Load " + f.toString() + " failure");
                }
            }
        }

        Log.i(Constants.TAG, "Loaded RuleSet " + cache.ruleSets.keySet());
    }

    private class Cache {
        General general;
        HashMap<String, RuleSet> ruleSets;
    }

    private StoreManager() {
        load();
    }

    private static StoreManager INSTANCE = new StoreManager();
    private Cache cache = new Cache();
    private Executor background = Executors.newSingleThreadExecutor();
}
