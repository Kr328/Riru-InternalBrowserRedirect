package com.github.kr328.ibr.remote.client;

import android.os.IBinder;

import com.github.kr328.ibr.remote.proxy.ServiceManagerProxy;

public class ClientInjector {
    public static void inject() throws ReflectiveOperationException {
        final ClientActivityManagerProxy proxy = new ClientActivityManagerProxy();

        ServiceManagerProxy.install(new ServiceManagerProxy.Callback() {
            @Override
            public IBinder getService(String name, IBinder service) {
                return proxy.proxy(name, service);
            }
        });
    }
}
