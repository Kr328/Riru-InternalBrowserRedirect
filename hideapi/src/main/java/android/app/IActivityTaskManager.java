package android.app;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IActivityTaskManager extends IInterface {
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

    class Stub extends Binder implements IActivityTaskManager {
        public static IActivityTaskManager asInterface(IBinder binder) {
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) {
            throw new IllegalArgumentException("Stub!");
        }

        @Override
        public IBinder asBinder() {
            throw new IllegalArgumentException("Stub!");
        }

        static class Proxy implements IActivityTaskManager {
            Proxy(IBinder remote) {
                throw new IllegalArgumentException("Stub!");
            }

            @Override
            public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) {
                throw new IllegalArgumentException("Stub!");
            }

            @Override
            public IBinder asBinder() {
                throw new IllegalArgumentException("Stub!");
            }
        }
    }
}
