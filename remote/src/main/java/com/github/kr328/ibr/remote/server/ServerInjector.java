package com.github.kr328.ibr.remote.server;

import android.os.ServiceManager;

import com.github.kr328.ibr.remote.shared.ServiceName;

public class ServerInjector {
    public static void inject() {
        ServiceManager.addService(ServiceName.SERVER, new RemoteService());
        ServiceManager.addService(ServiceName.CLIENT, new ClientService());
    }
}
