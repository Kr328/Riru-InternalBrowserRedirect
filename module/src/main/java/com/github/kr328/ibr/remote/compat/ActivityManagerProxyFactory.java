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
        if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.M )
            throw new ClassNotFoundException("Unsupported Android Version " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return createV23(original, callback);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            return createV26(original, callback);
        throw new ClassNotFoundException("Unsupported Android Version " + Build.VERSION.SDK_INT);
    }

    private static Binder createV23(Binder original, Callback callback) throws ReflectiveOperationException {
        return ProxyBinderFactory.createProxyBinder(original, (Binder) ((Class<?>) ActivityManagerProxyV23.class)
                .getConstructor(Binder.class, Callback.class)
                .newInstance(original, callback));
    }

    private static Binder createV26(Binder original, Callback callback) throws ReflectiveOperationException {
        return ProxyBinderFactory.createProxyBinder(original, (Binder) ((Class<?>) ActivityManagerProxyV26.class)
                .getConstructor(Binder.class, Callback.class)
                .newInstance(original, callback));
    }
}
