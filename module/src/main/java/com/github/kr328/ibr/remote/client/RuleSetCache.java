package com.github.kr328.ibr.remote.client;

import android.os.RemoteException;
import android.os.SystemProperties;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.model.RuleSet;

import java.util.HashMap;

class RuleSetCache {
    private HashMap<String, RuleSet> cache = new HashMap<>();
    private String lastUpdate = "";

    RuleSet getRuleSet(String packageName) throws RemoteException {
        String serviceLastUpdate = SystemProperties.get(Constants.LAST_UPDATE_KEY, "");
        if ( serviceLastUpdate.isEmpty() || !lastUpdate.equals(serviceLastUpdate) ) {
            cache.clear();
            lastUpdate = serviceLastUpdate;
        }

        RuleSet result = cache.get(packageName);
        if ( result == null )
            return cache.put(packageName, ClientConnection.getConnection().queryRuleSet(packageName));

        return result;
    }
}
