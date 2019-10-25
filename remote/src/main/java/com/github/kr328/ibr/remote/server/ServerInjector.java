package com.github.kr328.ibr.remote.server;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemProperties;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.proxy.ServiceManagerProxy;

public class ServerInjector {
    private static IBinder originalActivity;

    public static void inject() throws ReflectiveOperationException {
        ServiceManagerProxy.install(new ServiceManagerProxy.Callback() {
            @Override
            public IBinder addService(String name, IBinder service) {
                if (Context.ACTIVITY_SERVICE.equals(name)) {
                    originalActivity = service;

                    final RemoteService remoteService = new RemoteService();
                    final ClientService clientService = new ClientService();

                    SystemProperties.set(Constants.SERVICE_STATUE_KEY, "service_created");

                    return new ProxyBinder((Binder) service, (original, code, data, reply, flags) -> {
                        if (code == Constants.ACTIVITY_CONNECT_SERVER_CODE)
                            return remoteService.transactInstance(data, reply);
                        else if (code == Constants.ACTIVITY_CONNECT_CLIENT_CODE)
                            return clientService.transactInstance(data, reply);
                        return original.transact(code, data, reply, flags);
                    });
                }

                return super.addService(name, service);
            }

            @Override
            public IBinder getService(String name, IBinder service) {
                if (Context.ACTIVITY_SERVICE.equals(name))
                    return originalActivity;

                return super.getService(name, service);
            }

            @Override
            public IBinder checkService(String name, IBinder service) {
                if (Context.ACTIVITY_SERVICE.equals(name))
                    return originalActivity;

                return super.checkService(name, service);
            }
        });
    }
}
