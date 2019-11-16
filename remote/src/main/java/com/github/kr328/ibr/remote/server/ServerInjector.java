package com.github.kr328.ibr.remote.server;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.proxy.ServiceManagerProxy;

public class ServerInjector {
    private static IBinder original;

    public static void inject() {
        try {
            ServiceManagerProxy.install(new ServiceManagerProxy.Callback() {
                @Override
                public IBinder addService(String name, IBinder service) {
                    if (Context.ACTIVITY_SERVICE.equals(name))
                        return super.addService(name, new ActivityProxyBinder((Binder) (original = service)));

                    return super.addService(name, service);
                }

                @Override
                public IBinder getService(String name, IBinder service) {
                    if (Context.ACTIVITY_SERVICE.equals(name))
                        return original;

                    return super.getService(name, service);
                }

                @Override
                public IBinder checkService(String name, IBinder service) {
                    if (Context.ACTIVITY_SERVICE.equals(name))
                        return original;

                    return super.checkService(name, service);
                }
            });
        } catch (ReflectiveOperationException e) {
            Log.e(Constants.TAG, "Install proxy failure", e);
        }
    }
}
