package android.app;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

public abstract class ActivityManagerNative extends Binder implements IActivityManager {
    public static IActivityManager asInterface(IBinder binder) {
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public IBinder asBinder() {
        throw new IllegalArgumentException("Unsupported");
    }
}

class ActivityManagerProxy implements IActivityManager {
    public ActivityManagerProxy(IBinder remote) {
        throw new IllegalArgumentException("Stub!");
    }

    @Override
    public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
        throw new IllegalArgumentException("Stub!");
    }

    @Override
    public IBinder asBinder() {
        throw new IllegalArgumentException("Stub!");
    }
}