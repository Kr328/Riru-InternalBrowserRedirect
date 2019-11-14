package com.github.kr328.ibr.remote.server;

import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.shared.ServiceName;

public class ServerInjector {
    public static void inject() {
        new Thread() {
            @Override
            public void run() {
                while ( !"true".equals(SystemProperties.get(Constants.SCRIPT_SELINUX_POLICY_INJECT, "false")) ) {
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                    Log.i(Constants.TAG, "Waiting selinux inject for 1s");
                }
                Log.i(Constants.TAG, "Injecting SystemServer");
                ServiceManager.addService(ServiceName.SERVER, new RemoteService());
                ServiceManager.addService(ServiceName.CLIENT, new ClientService());
            }
        }.start();
    }
}
