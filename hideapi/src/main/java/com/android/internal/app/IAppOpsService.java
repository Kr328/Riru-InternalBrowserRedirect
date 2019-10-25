package com.android.internal.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IAppOpsService extends IInterface {
    int checkOperation(int code, int uid, String packageName) throws RemoteException;

    int noteOperation(int code, int uid, String packageName) throws RemoteException;

    public abstract class Stub extends Binder implements IAppOpsService {
        public static IAppOpsService asInterface(IBinder binder) {
            throw new IllegalArgumentException("Stub!");
        }

        @Override
        public IBinder asBinder() {
            throw new IllegalArgumentException("Stub!");
        }
    }
}
