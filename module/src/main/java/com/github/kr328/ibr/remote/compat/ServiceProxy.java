package com.github.kr328.ibr.remote.compat;

import android.app.IActivityManager;
import android.app.IActivityTaskManager;
import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;

import com.github.kr328.ibr.remote.proxy.IBinderProxy;

import java.lang.reflect.Proxy;

public abstract class ServiceProxy {
    public IBinder proxy(String name, IBinder original) {
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
                            if ( method.getName().equals("startActivity") &&
                                    (args[1] instanceof String) &&
                                    (args[2] instanceof Intent) ) {
                                args[2] = handleStartActivity((Intent)args[2], (String)args[1]);
                            }
                            else if ( method.getName().equals("asBinder") && args.length == 0 ) {
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
                            if ( method.getName().equals("startActivity") &&
                                    (args[1] instanceof String) &&
                                    (args[2] instanceof Intent)) {
                                args[2] = handleStartActivity((Intent)args[2], (String)args[1]);
                            }
                            else if ( method.getName().equals("asBinder") && args.length == 0 ) {
                                return original;
                            }
                            return method.invoke(am, args);
                        });
            }

            return i;
        });
    }

    protected abstract Intent handleStartActivity(Intent intent, String callingPackage);
}
