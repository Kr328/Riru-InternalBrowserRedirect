package com.github.kr328.ibr.remote;

import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemProperties;
import android.util.Log;

import com.github.kr328.ibr.remote.compat.ActivityManagerProxyFactory;
import com.github.kr328.ibr.remote.proxy.ServiceManagerProxy;

@SuppressWarnings("unused")
public class Injector {
    private static Binder originalActivityManager;

    public static void inject(String argument) {
        try {
            Log.i(Constants.TAG, "In system_server pid = " + android.os.Process.myPid() + " SDK_INT = " + Build.VERSION.SDK_INT);

            ServiceManagerProxy.install(new ServiceManagerProxy.Callback() {
                @Override
                public IBinder addService(String name, IBinder original) {
                    Log.d(Constants.TAG, "addService " + name);

                    try {
                        if (Context.ACTIVITY_SERVICE.equals(name)) {
                            originalActivityManager = (Binder) original;

                            Binder proxy = ActivityManagerProxyFactory.create(originalActivityManager, new ActivityManagerProxy());

                            SystemProperties.set(Constants.SERVICE_STATUE_KEY, "service_created");

                            return proxy;
                        }
                    } catch (ReflectiveOperationException e) {
                        Log.e(Constants.TAG, "Create proxy failure", e);
                    }

                    return original;
                }

                @Override
                public IBinder getService(String name, IBinder original) {
                    if (Context.ACTIVITY_SERVICE.equals(name) && originalActivityManager != null && original != null)
                        return originalActivityManager;
                    return original;
                }

                @Override
                public IBinder checkService(String name, IBinder original) {
                    if (Context.ACTIVITY_SERVICE.equals(name) && originalActivityManager != null && original != null)
                        return originalActivityManager;
                    return original;
                }
            });
        } catch (ReflectiveOperationException e) {
            Log.e(Constants.TAG, "Inject failure", e);
        }

        SystemProperties.set(Constants.SERVICE_STATUE_KEY, "inject_success");

        Log.i(Constants.TAG, "Inject successfully");
    }
}
