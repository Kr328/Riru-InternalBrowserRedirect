package android.app;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IActivityManager extends IInterface {
    int startActivity(IApplicationThread caller,  // 0
                      String callingPackage,      // 1
                      Intent intent,              // 2
                      String resolvedType,        // 3
                      IBinder resultTo,           // 4
                      String resultWho,           // 5
                      int requestCode,            // 6
                      int flags,                  // 7
                      ProfilerInfo profilerInfo,  // 8
                      Bundle options              // 9
    ) throws RemoteException;

    void forceStopPackage(String packageName, int userId) throws RemoteException;

    abstract class Stub extends Binder implements IActivityManager {
        public static IActivityManager asInterface(IBinder binder) {
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        public IBinder asBinder() {
            throw new IllegalArgumentException("Unsupported");
        }
    }
}
