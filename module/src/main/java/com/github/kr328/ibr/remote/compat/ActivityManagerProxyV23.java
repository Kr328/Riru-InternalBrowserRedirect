package com.github.kr328.ibr.remote.compat;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.IApplicationThread;
import android.app.ProfilerInfo;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.proxy.ProxyBinderFactory.CustomTransact;
import com.github.kr328.ibr.remote.proxy.ProxyBinderFactory.ReplaceTransact;

@CustomTransact({Constants.ACTIVITY_CONNECT_TRANSACT_CODE})
public class ActivityManagerProxyV23 extends ActivityManagerNative {
    private IActivityManager activityManager;
    private ActivityManagerProxyFactory.Callback callback;

    public ActivityManagerProxyV23(Binder original, ActivityManagerProxyFactory.Callback callback) throws NoSuchMethodException {
        this.activityManager = asInterface(original);
        this.callback = callback;

        if (activityManager == null)
            throw new NoSuchMethodException("Invalid ActivityManager");
    }

    @Override
    @ReplaceTransact
    public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType,
                             IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
        return activityManager.startActivity(caller, callingPackage, callback.startActivity(callingPackage, intent),
                resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        if (code == Constants.ACTIVITY_CONNECT_TRANSACT_CODE) {
            data.enforceInterface(Constants.APPLICATION_ID);

            reply.writeStrongBinder(callback.queryRedirectService());

            return true;
        }

        return super.onTransact(code, data, reply, flags);
    }
}
