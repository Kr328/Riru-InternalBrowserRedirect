package com.github.kr328.ibr.remote.client;

import android.os.RemoteException;
import android.os.ServiceManager;

import com.github.kr328.ibr.remote.shared.IClientService;
import com.github.kr328.ibr.remote.shared.ServiceName;

class ClientConnection {
    private static IClientService connection;

    static IClientService getConnection() throws RemoteException {
        if (connection == null)
            openConnection();

        return connection;
    }

    private static void openConnection() throws RemoteException {
        connection = IClientService.Stub.asInterface(ServiceManager.getService(ServiceName.CLIENT));

        if (connection == null)
            throw new RemoteException("Unable to connect system_server");
    }
}
