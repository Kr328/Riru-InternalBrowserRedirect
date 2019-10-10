package com.github.kr328.ibr.remote.server;

import android.os.Parcel;
import android.os.RemoteException;

import com.github.kr328.ibr.remote.model.RuleSet;

public class ClientService extends IClientService.Stub {
    @Override
    public RuleSet queryRuleSet(String packageName) {
        return StoreManager.getInstance().getRuleSet(packageName);
    }

    boolean transactInstance(Parcel data, Parcel reply) {
        data.enforceInterface(IClientService.class.getName());

        reply.writeStrongBinder(this);

        return true;
    }
}