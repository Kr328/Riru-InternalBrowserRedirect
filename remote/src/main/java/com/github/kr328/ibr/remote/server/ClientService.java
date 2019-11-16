package com.github.kr328.ibr.remote.server;

import android.os.Parcel;

import com.github.kr328.ibr.remote.shared.IClientService;
import com.github.kr328.ibr.remote.shared.RuleSet;

public class ClientService extends IClientService.Stub {
    boolean transact(Parcel data, Parcel reply) {
        data.enforceInterface(IClientService.class.getName());

        reply.writeStrongBinder(this);

        return true;
    }

    @Override
    public RuleSet queryRuleSet(String packageName) {
        return StoreManager.getInstance().getRuleSet(packageName);
    }
}
