package com.github.kr328.ibr.remote;

import android.os.Build;
import android.util.Log;

import com.github.kr328.ibr.remote.client.ClientInjector;
import com.github.kr328.ibr.remote.proxy.ServiceManagerProxy;

@SuppressWarnings("unused")
public class Injector {
    public static void inject(String argument) {
        switch (argument) {
            case "init":
                injectInitialize();
                break;
            case "app_forked":
                injectApplication();
                break;
            case "system_server_forked":
                injectSystemServer();
                break;
        }
    }

    private static void injectInitialize() {
        Log.i(Constants.TAG, "Initializing");

        try {
            ServiceManagerProxy.install();
        } catch (ReflectiveOperationException e) {
            Log.e(Constants.TAG, "Inject failure", e);
            return;
        }

        Log.i(Constants.TAG, "Inject successfully");
    }

    private static void injectSystemServer() {
        Log.i(Constants.TAG, "In system_server pid = " + android.os.Process.myPid() + " SDK_INT = " + Build.VERSION.SDK_INT);
    }

    private static void injectApplication() {
        Log.i(Constants.TAG, "In application pid = " + android.os.Process.myPid());

        ClientInjector.inject();
    }
}
