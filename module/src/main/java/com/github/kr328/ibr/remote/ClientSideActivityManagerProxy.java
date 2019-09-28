package com.github.kr328.ibr.remote;

import android.content.Intent;
import android.os.SystemProperties;
import android.util.Log;

import com.github.kr328.ibr.remote.compat.ServiceProxy;

public class ClientSideActivityManagerProxy extends ServiceProxy {
    @Override
    protected Intent handleStartActivity(Intent intent, String callingPackage) {
        if ( "true".equals(SystemProperties.get(Constants.DEBUG_MODE_KEY, "false")) ) {
            for (String line : Logger.log(callingPackage, (Intent) intent.clone()).split("\n"))
                Log.d(Constants.TAG, line);
        }

        return intent;
    }
}
