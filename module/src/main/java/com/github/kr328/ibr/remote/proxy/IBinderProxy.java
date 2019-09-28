package com.github.kr328.ibr.remote.proxy;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import com.github.kr328.ibr.remote.Constants;

import java.io.FileDescriptor;

public class IBinderProxy implements IBinder {
    private IBinder original;
    private Callback callback;

    public interface Callback {
        IInterface onQueryLocalInterface(String descriptor, IInterface original);
    }

    public IBinderProxy(IBinder original, Callback callback) {
        this.original = original;
        this.callback = callback;
    }

    @Override
    public String getInterfaceDescriptor() throws RemoteException {
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
    public IInterface queryLocalInterface(String descriptor) {
        return callback.onQueryLocalInterface(descriptor, original.queryLocalInterface(descriptor));
    }

    @Override
    public void dump(FileDescriptor fd, String[] args) throws RemoteException {
        original.dump(fd, args);
    }

    @Override
    public void dumpAsync(FileDescriptor fd, String[] args) throws RemoteException {
        original.dumpAsync(fd, args);
    }

    @Override
    public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        return original.transact(code, data, reply, flags);
    }

    @Override
    public void linkToDeath(DeathRecipient recipient, int flags) throws RemoteException {
        original.linkToDeath(recipient, flags);
    }

    @Override
    public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
        return original.unlinkToDeath(recipient, flags);
    }
}
