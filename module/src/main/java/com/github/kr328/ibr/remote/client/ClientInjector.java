package com.github.kr328.ibr.remote.client;

import android.os.IBinder;

import com.github.kr328.ibr.remote.model.RuleSet;
import com.github.kr328.ibr.remote.proxy.ServiceManagerProxy;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientInjector {
    public static void inject() throws ReflectiveOperationException, JSONException {
        final ClientActivityManagerProxy proxy = new ClientActivityManagerProxy(new RuleSet());

        ServiceManagerProxy.install(new ServiceManagerProxy.Callback() {
            @Override
            public IBinder getService(String name, IBinder service) {
                return proxy.proxy(name, service);
            }
        });
    }
}
