package com.github.kr328.ibr.remote.server;

import android.os.Parcel;
import android.os.RemoteException;

import com.github.kr328.ibr.remote.shared.IClientService;
import com.github.kr328.ibr.remote.shared.RuleSet;

public class ClientService extends IClientService.Stub {
    boolean transactInstance(Parcel data, Parcel reply) {
        data.enforceInterface(IClientService.class.getName());

        reply.writeStrongBinder(this);

        return true;
    }

    @Override
    public RuleSet queryRuleSet(String packageName) throws RemoteException {
        return StoreManager.getInstance().getRuleSet(packageName);
    }
}
