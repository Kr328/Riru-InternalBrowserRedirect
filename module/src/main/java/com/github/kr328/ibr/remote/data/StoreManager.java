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
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;

public class StoreManager {
    public static StoreManager getInstance() {
        if ( INSTANCE == null )
            INSTANCE = new StoreManager();
        return INSTANCE;
    }

    public synchronized RuleSet getRuleSet(String pkg) {
        RuleSet ruleSet;
        if ((ruleSet = cache.changedRuleSets.get(pkg)) != null)
            return ruleSet;
        if ((ruleSet = cache.ruleSets.get(pkg)) != null)
            return ruleSet;
        return null;
    }

    public synchronized Map<String, RuleSet> getRuleSets() {
        HashMap<String, RuleSet> result = new HashMap<>();

        result.putAll(cache.ruleSets);
        result.putAll(cache.changedRuleSets);

        return result;
    }

    public synchronized General getGeneral() {
        return cache.general;
    }

    public synchronized void updateRuleSet(String pkg, RuleSet ruleSet) {
        cache.changedRuleSets.put(pkg, ruleSet);
        saveTimer.cancel();
        saveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                save();
            }
        }, 1000);
    }

    private StoreManager() { load(); }

    private static StoreManager INSTANCE;
    private Cache cache = new Cache();
    private Timer saveTimer = new Timer();

    private synchronized void load() {
        cache.ruleSets = new HashMap<>();
        cache.changedRuleSets = new HashMap<>();

        try {
            cache.general = General.parseFromJson(new JSONObject(FileUtils.readLines(Constants.DATA_STORE_DIRECTORY + "/general.json")));
        } catch (IOException|JSONException e) {
            Log.w(Constants.TAG, "Load general config failure", e);
            cache.general = new General();
        }

        File[] files = new File(Constants.DATA_STORE_DIRECTORY).listFiles();
        if ( files == null )
            return;

        for ( File f : files ) {
            Matcher matcher = Constants.PATTERN_CONFIG_FILE.matcher(f.getName());
            if ( matcher.matches() ) {
                try {
                    cache.ruleSets.put(matcher.group(1), RuleSet.readFromJson(new JSONObject(FileUtils.readLines(f))));
                }
                catch (IOException|JSONException e) {
                    Log.w(Constants.TAG, "Load " + f.toString() + " failure");
                }
            }
        }

        Log.i(Constants.TAG, "Loaded RuleSet " + cache.ruleSets.keySet());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void save() {
        new File(Constants.DATA_STORE_DIRECTORY).mkdirs();

        Set<Map.Entry<String, RuleSet>> changed;
        synchronized (this) {
            changed = cache.changedRuleSets.entrySet();
            cache.ruleSets.putAll(cache.changedRuleSets);
            cache.changedRuleSets.clear();
        }

        for ( Map.Entry<String, RuleSet> entry : changed ) {
            try {
                if ( entry.getValue().getRules().size() > 0 )
                    FileUtils.writeLines(new File(Constants.DATA_STORE_DIRECTORY,
                            String.format(Constants.TEMPLATE_CONFIG_FILE, entry.getKey())),
                            entry.getValue().toJson().toString());
                else
                    new File(String.format(Constants.TEMPLATE_CONFIG_FILE, entry.getKey())).delete();
            } catch (IOException|JSONException e) {
                Log.w(Constants.TAG, "Save config " + entry.getKey() + " failure", e);
            }
        }
    }

    private class Cache {
        General general;
        HashMap<String, RuleSet> ruleSets;
        HashMap<String, RuleSet> changedRuleSets;
    }
}
