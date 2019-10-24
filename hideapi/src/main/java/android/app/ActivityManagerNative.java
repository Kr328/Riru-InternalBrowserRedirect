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