package com.github.kr328.ibr.remote;

import android.os.Build;
import android.util.Log;

import com.github.kr328.ibr.remote.client.ClientInjector;
import com.github.kr328.ibr.remote.proxy.ServiceManagerProxy;
import com.github.kr328.ibr.remote.server.ServerInjector;

import org.json.JSONException;

@SuppressWarnings("unused")
public class Injector {
    public static void inject(String argument) {
        try {
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
        } catch (Throwable e) {
            Log.e(Constants.TAG, "Inject " + argument + " failure", e);
        }
    }

    private static void injectInitialize() throws ReflectiveOperationException {
        Log.i(Constants.TAG, "Initializing");

        ServiceManagerProxy.install();

        Log.i(Constants.TAG, "Inject successfully");
    }

    private static void injectSystemServer() throws ReflectiveOperationException {
        Log.i(Constants.TAG, "In system_server pid = " + android.os.Process.myPid() + " SDK_INT = " + Build.VERSION.SDK_INT);

        ServerInjector.inject();

        Log.i(Constants.TAG, "Inject successfully");
    }

    private static void injectApplication() throws ReflectiveOperationException, JSONException {
        Log.i(Constants.TAG, "In application pid = " + android.os.Process.myPid());

        ClientInjector.inject();

        Log.i(Constants.TAG, "Inject successfully");
    }
}
