package android.app;

import android.os.IBinder;

public class ActivityManagerProxyV23 extends IActivityTaskManager.Stub.Proxy {
    public ActivityManagerProxyV23(IBinder remote) {
        super(remote);
    }
}
