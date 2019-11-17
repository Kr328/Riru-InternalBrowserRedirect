package com.github.kr328.ibr.remote.client;

import android.app.ActivityThread;
import android.content.pm.ApplicationInfo;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.shared.IClientService;
import com.github.kr328.ibr.remote.shared.RuleSet;

class RuleSetCache {
    private RuleSet ruleSet;
    private String lastUpdate = "";

    RuleSet getRuleSet(String fallback) throws RemoteException {
        String serviceLastUpdate = SystemProperties.get(Constants.LAST_UPDATE_KEY, "");
        if (serviceLastUpdate.isEmpty() || !lastUpdate.equals(serviceLastUpdate)) {
            ruleSet = null;
            lastUpdate = serviceLastUpdate;
        }

        IClientService connection = ClientConnection.getConnection();
        if (ruleSet == null && connection != null)
            return ruleSet = connection.queryRuleSet(findPackageName(fallback));

        return ruleSet;
    }

    private String findPackageName(String fallback) {
        try {
            ApplicationInfo application = ActivityThread.currentActivityThread()
                    .getApplication().getApplicationInfo();

            if ( application.packageName.isEmpty() )
                return fallback;

            return application.packageName;
        }
        catch (Throwable throwable) {
            Log.w(Constants.TAG, "Find package name failure", throwable);
            return fallback;
        }
    }
}
