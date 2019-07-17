package com.github.kr328.ibr;

import android.app.IActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import com.github.kr328.ibr.proxy.ProxyBinder;
import com.github.kr328.ibr.proxy.ProxyBinderFactory;
import com.github.kr328.ibr.proxy.ServiceManagerProxy;

@SuppressWarnings("unused")
public class Injector {
    private static Binder originalActivityManager;

    public static void inject(String argument) {
        try {
            ServiceManagerProxy.install(new ServiceManagerProxy.Callback() {
                @Override
                public IBinder addService(String name, IBinder original) {
                    try {
                        if ( Context.ACTIVITY_SERVICE.equals(name) ) {
                            StoreManager.getInstance();
                            originalActivityManager = (Binder) original;
                            return ProxyBinderFactory.createProxyBinder(originalActivityManager,
                                    new ActivityManagerProxy(IActivityManager.Stub.asInterface(originalActivityManager)));
                        }
                    }
                    catch (ReflectiveOperationException e) {
                        Log.e(Constants.TAG, "Create proxy failure", e);
                    }

                    return original;
                }

                @Override
                public IBinder getService(String name, IBinder original) {
                    if ( Context.ACTIVITY_SERVICE.equals(name) && originalActivityManager != null && original != null )
                        return originalActivityManager;
                    return original;
                }

                @Override
                public IBinder checkService(String name, IBinder original) {
                    if ( Context.ACTIVITY_SERVICE.equals(name) && originalActivityManager != null && original != null )
                        return originalActivityManager;
                    return original;
                }
            });
        } catch (ReflectiveOperationException e) {
            Log.e(Constants.TAG, "Inject failure", e);
        }

        Log.i(Constants.TAG, "Inject successfully");
    }
}
