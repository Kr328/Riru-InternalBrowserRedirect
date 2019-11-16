package com.github.kr328.ibr.remote.client;

import android.content.Context;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.github.kr328.ibr.remote.shared.IClientService;
import com.github.kr328.ibr.remote.shared.ServiceHandle;

class ClientConnection {
    private static IClientService connection;

    static IClientService getConnection() throws RemoteException {
        if (connection == null)
            openConnection();

        return connection;
    }

    private static void openConnection() throws RemoteException {
        IBinder activity = ServiceManager.getService(Context.ACTIVITY_SERVICE);

        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        try {
            data.writeInterfaceToken(IClientService.class.getName());

            activity.transact(ServiceHandle.CLIENT, data, reply, 0);

            IBinder client = reply.readStrongBinder();

            if (client == null || (connection = IClientService.Stub.asInterface(client)) == null)
                throw new RemoteException("Unable to open connection");
        } finally {
            data.recycle();
            reply.recycle();
        }
    }
}
