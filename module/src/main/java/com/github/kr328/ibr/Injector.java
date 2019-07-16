package com.github.kr328.ibr;

import android.app.IActivityManager;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.github.kr328.ibr.proxy.ProxyBinderFactory;
import com.github.kr328.ibr.proxy.ServiceManagerProxy;

@SuppressWarnings("unused")
public class Injector {
    public static void inject(String argument) {
        try {
            ServiceManagerProxy.install(new ServiceManagerProxy.Callback() {
                @Override
                public IBinder addService(String name, IBinder original) {
                    try {
                        if ( Context.ACTIVITY_SERVICE.equals(name) )
                            return ProxyBinderFactory.createProxyBinder((Binder) original,
                                    new ActivityManagerProxy(IActivityManager.Stub.asInterface(original)));
                    }
                    catch (ReflectiveOperationException e) {
                        Log.e(Constants.TAG, "Create activity proxy failure", e);
                    }

                    return original;
                }

                @Override
                public IBinder getService(String name, IBinder original) {
                    return original;
                }

                @Override
                public IBinder checkService(String name, IBinder original) {
                    return original;
                }
            });
        } catch (ReflectiveOperationException e) {
            Log.e(Constants.TAG, "Inject failure", e);
        }

        Log.i(Constants.TAG, "Inject successfully");
    }
}
