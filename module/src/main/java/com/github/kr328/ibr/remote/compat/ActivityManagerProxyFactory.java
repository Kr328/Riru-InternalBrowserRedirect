package com.github.kr328.ibr.remote.compat;

import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.RemoteException;

import com.github.kr328.ibr.remote.proxy.ProxyBinderFactory;

public class ActivityManagerProxyFactory {
    public interface Callback {
        Intent startActivity(String callingPackage, Intent intent) throws RemoteException;

        Binder queryRedirectService() throws RemoteException;
    }

    public static Binder create(Binder original, Callback callback) throws ReflectiveOperationException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return ProxyBinderFactory.createProxyBinder(original, new ActivityManagerProxy(original, callback));
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            return ProxyBinderFactory.createProxyBinder(original, new ActivityManagerProxyV26(original, callback));
        else
            throw new ClassNotFoundException("Unsupported Android Version " + Build.VERSION.SDK_INT);
    }
}
