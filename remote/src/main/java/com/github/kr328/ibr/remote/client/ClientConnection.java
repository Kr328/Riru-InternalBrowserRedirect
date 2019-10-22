package com.github.kr328.ibr.remote.client;

import android.os.IBinder;
import android.os.Parcel;
import android.os.ServiceManager;
import android.util.Log;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.server.IClientService;

class ClientConnection {
    private static IClientService connection;

    static IClientService getConnection() {
        if ( connection == null )
            openConnection();

        return connection;
    }

    private static void openConnection() {
        IBinder binder = ServiceManager.getService("activity");

        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        try {
            data.writeInterfaceToken(IClientService.class.getName());

            binder.transact(Constants.ACTIVITY_CONNECT_CLIENT_CODE, data, reply, 0);

            connection = IClientService.Stub.asInterface(reply.readStrongBinder());
        } catch (Exception e) {
            Log.e(Constants.TAG, "Unable to connect client service", e);
            connection = new IClientService.Default();
        }
        finally {
            data.recycle();
            reply.recycle();
        }
    }
}
