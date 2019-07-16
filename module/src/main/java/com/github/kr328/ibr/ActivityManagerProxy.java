package com.github.kr328.ibr;

import android.app.IActivityManager;
import android.app.IApplicationThread;
import android.app.ProfilerInfo;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.github.kr328.ibr.proxy.ProxyBinderFactory;
import com.github.kr328.ibr.proxy.ProxyBinderFactory.ReplaceTransact;

public class ActivityManagerProxy extends IActivityManager.Stub {
    private IActivityManager original;

    ActivityManagerProxy(IActivityManager original) {
        this.original = original;
    }

    @Override
    @ReplaceTransact
    public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) {
        Log.d(Constants.TAG, intent.toString());

        return original.startActivity(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);
    }
}
