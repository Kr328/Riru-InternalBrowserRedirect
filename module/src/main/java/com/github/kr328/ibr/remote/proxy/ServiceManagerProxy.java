package com.github.kr328.ibr.remote.proxy;

import android.annotation.SuppressLint;
import android.os.Binder;
import android.os.IBinder;
import android.os.IPermissionController;
import android.os.IServiceManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManagerNative;
import android.util.Log;

import com.android.internal.os.BinderInternal;
import com.github.kr328.ibr.remote.Constants;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

@SuppressWarnings("JavaReflectionMemberAccess")
@SuppressLint("PrivateApi")
public class ServiceManagerProxy implements IServiceManager {
    public interface Callback {
        IBinder addService(String name, IBinder original);

        IBinder getService(String name, IBinder original);

        IBinder checkService(String name, IBinder original);
    }

    public static synchronized void install(Callback callback) throws ReflectiveOperationException {
        if (installed)
            return;

        IServiceManager original = getOriginalIServiceManager();

        setDefaultServiceManager(new ServiceManagerProxy(original, callback));

        installed = true;
    }

    private IServiceManager original;
    private Callback callback;
    private static boolean installed;
    private static Method allowBlocking;

    private ServiceManagerProxy(IServiceManager original, Callback callback) {
        this.original = original;
        this.callback = callback;
    }

    private static IServiceManager getOriginalIServiceManager() throws ReflectiveOperationException {
        Field field = ServiceManager.class.getDeclaredField("sServiceManager");
        field.setAccessible(true);
        return (IServiceManager) field.get(null);
    }

    private static void setDefaultServiceManager(IServiceManager serviceManager) throws ReflectiveOperationException {
        Field field = ServiceManager.class.getDeclaredField("sServiceManager");
        field.setAccessible(true);
        field.set(null, serviceManager);
    }

    private IServiceManager getOriginal() {
        if ( original == null ) {
            try {
                original = ServiceManagerNative
                        .asInterface((IBinder) allowBlocking.invoke(null, BinderInternal.getContextObject()));
            } catch (Exception e) {
                Log.w(Constants.TAG, "allowBlocking failure", e);
                original = ServiceManagerNative.asInterface(BinderInternal.getContextObject());
            }
        }

        return original;
    }

    // Pie
    @Override
    public IBinder getService(String name) throws RemoteException {
        return callback.getService(name, getOriginal().getService(name));
    }

    @Override
    public IBinder checkService(String name) throws RemoteException {
        return callback.checkService(name, getOriginal().checkService(name));
    }

    @Override
    public void addService(String name, IBinder service, boolean allowIsolated, int dumpFlags) throws RemoteException {
        getOriginal().addService(name, callback.addService(name, service), allowIsolated, dumpFlags);
    }

    @Override
    public String[] listServices(int dumpFlags) throws RemoteException {
        return getOriginal().listServices(dumpFlags);
    }

    @Override
    public void setPermissionController(IPermissionController controller) throws RemoteException {
        getOriginal().setPermissionController(controller);
    }

    // Oreo
    @Override
    public void addService(String name, IBinder service, boolean allowIsolated) throws RemoteException {
        getOriginal().addService(name, callback.addService(name, service), allowIsolated);
    }

    @Override
    public String[] listServices() throws RemoteException {
        return getOriginal().listServices();
    }

    @Override
    public IBinder asBinder() {
        return getOriginal().asBinder();
    }

     static {
         try {
             allowBlocking = Binder.class.getDeclaredMethod("allowBlocking", IBinder.class);
         } catch (NoSuchMethodException e) {
             Log.e(Constants.TAG, "allowBlocking", e);
         }
     }
}
