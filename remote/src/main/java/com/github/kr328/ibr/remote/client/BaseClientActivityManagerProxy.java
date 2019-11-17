package com.github.kr328.ibr.remote.client;

import android.app.IActivityManager;
import android.app.IActivityTaskManager;
import android.app.IApplicationThread;
import android.app.ProfilerInfo;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.proxy.IBinderProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

abstract class BaseClientActivityManagerProxy {
    private static final Method startActivity;
    private static final Method startActivityTask;
    private static final Method asBinder;

    static {
        Method sa = null;
        Method sat = null;
        Method sb = null;

        try {
            sa = IActivityManager.class.getMethod("startActivity",
                    IApplicationThread.class, String.class, Intent.class,
                    String.class, IBinder.class, String.class, int.class,
                    int.class, ProfilerInfo.class, Bundle.class);
        } catch (NoSuchMethodException e) {
            Log.e(Constants.TAG, "IActivityManager.startActivity not found", e);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                sat = IActivityTaskManager.class.getMethod("startActivity",
                        IApplicationThread.class, String.class, Intent.class,
                        String.class, IBinder.class, String.class, int.class,
                        int.class, ProfilerInfo.class, Bundle.class);
            } catch (NoSuchMethodException e) {
                Log.e(Constants.TAG, "IActivityTaskManager.startActivity not found", e);
            }
        }

        try {
            sb = IInterface.class.getMethod("asBinder");
        } catch (NoSuchMethodException e) {
            Log.e(Constants.TAG, "IInterface.asBinder", e);
        }

        startActivity = sa;
        startActivityTask = sat;
        asBinder = sb;
    }

    protected abstract void handleStartActivity(StartActivityPayloads payloads);

    IBinder proxy(String name, IBinder original) {
        switch (name) {
            case "activity":
                return proxyActivityManager(original);
            case "activity_task":
                return proxyActivityTaskManager(original);
        }
        return original;
    }

    private IBinder proxyActivityManager(IBinder original) {
        return new IBinderProxy(original, (descriptor, i) -> {
            if (IActivityManager.class.getName().equals(descriptor)) {
                IActivityManager am = IActivityManager.Stub.asInterface(original);

                return (IInterface) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                        new Class[]{IActivityManager.class},
                        (instance, method, args) -> {
                            if (method.equals(startActivity)) {
                                StartActivityPayloads payloads = new StartActivityPayloads();

                                payloads.callingPackage = (String) args[1];
                                payloads.intent = (Intent) args[2];
                                payloads.options = (Bundle) args[9];

                                handleStartActivity(payloads);

                                args[1] = payloads.callingPackage;
                                args[2] = payloads.intent;
                                args[9] = payloads.options;
                            } else if (method.equals(asBinder)) {
                                return original;
                            }
                            return method.invoke(am, args);
                        });
            }

            return i;
        });
    }

    private IBinder proxyActivityTaskManager(IBinder original) {
        return new IBinderProxy(original, (descriptor, i) -> {
            if (IActivityTaskManager.class.getName().equals(descriptor)) {
                IActivityTaskManager am = IActivityTaskManager.Stub.asInterface(original);

                return (IInterface) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                        new Class[]{IActivityTaskManager.class},
                        (instance, method, args) -> {
                            if (method.equals(startActivityTask)) {
                                StartActivityPayloads payloads = new StartActivityPayloads();

                                payloads.callingPackage = (String) args[1];
                                payloads.intent = (Intent) args[2];
                                payloads.options = (Bundle) args[9];

                                handleStartActivity(payloads);

                                args[1] = payloads.callingPackage;
                                args[2] = payloads.intent;
                                args[9] = payloads.options;
                            } else if (method.equals(asBinder)) {
                                return original;
                            }
                            return method.invoke(am, args);
                        });
            }

            return i;
        });
    }

    static class StartActivityPayloads {
        String callingPackage;
        Intent intent;
        Bundle options;
    }
}
