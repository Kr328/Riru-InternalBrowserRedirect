package com.github.kr328.ibr.remote.server;

import android.os.Binder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.github.kr328.ibr.remote.shared.ServiceHandle;

public class ActivityProxyBinder extends Binder {
    private ClientService clientService = new ClientService();
    private RemoteService remoteService = new RemoteService();
    private Binder original;

    public ActivityProxyBinder(Binder original) {
        this.original = original;
    }

    // for other module to reflect original binder
    @SuppressWarnings("unused")
    public Binder getOriginal() {
        return original;
    }

    @Override
    public IInterface queryLocalInterface(String descriptor) {
        return null;
    }

    @Override
    public void attachInterface(IInterface owner, String descriptor) {
    }

    @Override
    public String getInterfaceDescriptor() {
        return original.getInterfaceDescriptor();
    }

    @Override
    public boolean pingBinder() {
        return original.pingBinder();
    }

    @Override
    public boolean isBinderAlive() {
        return original.isBinderAlive();
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        if (code == ServiceHandle.CLIENT)
            return clientService.transact(data, reply);
        else if (code == ServiceHandle.SERVER)
            return remoteService.transact(data, reply);
        return original.transact(code, data, reply, flags);
    }

    @Override
    public void linkToDeath(DeathRecipient deathRecipient, int i) {
        original.linkToDeath(deathRecipient, i);
    }

    @Override
    public boolean unlinkToDeath(DeathRecipient deathRecipient, int i) {
        return original.unlinkToDeath(deathRecipient, i);
    }
}