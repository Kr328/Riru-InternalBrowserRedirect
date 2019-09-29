package com.github.kr328.ibr.remote.server;

import android.content.pm.IPackageManager;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.model.RuleSet;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

public class RemoteService extends IRemoteService.Stub {
    class ZygoteQuerySocketThread extends Thread {
        @Override
        public void run() {
            try {
                LocalServerSocket socket = new LocalServerSocket(Constants.ZYGOTE_SOCKET_NAME);

                while ( !isInterrupted() ) {
                    LocalSocket client = socket.accept();

                    new Thread(() -> {
                        try {
                            if ( client.getPeerCredentials().getUid() != 0 ) {
                                client.close();
                                return;
                            }

                            DataInputStream inputStream = new DataInputStream(client.getInputStream());
                            DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());

                            while ( !isInterrupted() ) {
                                int length = inputStream.readInt();
                                byte[] buffer = new byte[length];
                                int read = 0;

                                while ( read < length ) {
                                    read += inputStream.read(buffer, read, length - read);
                                }

                                String packageName = new String(buffer);

                                if ( StoreManager.getInstance().getRuleSet(packageName) != null )
                                    outputStream.writeInt(1);
                                else
                                    outputStream.writeInt(0);
                            }
                        } catch (IOException e) {
                            Log.w(Constants.TAG, "Client connection close", e);
                        }
                    });
                }
            } catch (IOException e) {
                Log.e(Constants.TAG, "Create zygote local socket failure", e);
            }
        }
    }

    RemoteService() {
        new ZygoteQuerySocketThread().start();
    }

    private IPackageManager packageManager;

    private IPackageManager getPackageManager() {
        if (packageManager == null)
            packageManager = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
        return packageManager;
    }

    private void enforcePermission() throws RemoteException {
        String[] pkgs = getPackageManager().getPackagesForUid(Binder.getCallingUid());
        for (String pkg : pkgs) {
            if (Constants.APPLICATION_ID.equals(pkg))
                return;
        }
        throw new RemoteException("Permission denied");
    }

    boolean transactInstance(Parcel data, Parcel reply) throws RemoteException {
        SystemProperties.set(Constants.SERVICE_STATUE_KEY, "running");

        data.enforceInterface(Constants.APPLICATION_ID);

        enforcePermission();

        reply.writeStrongBinder(this);

        return true;
    }

    @Override
    public int getVersion() {
        return Constants.VERSION;
    }

    @Override
    public Map queryAllRuleSet() {
        return StoreManager.getInstance().getRuleSets();
    }

    @Override
    public RuleSet queryRuleSet(String packageName) {
        return StoreManager.getInstance().getRuleSet(packageName);
    }

    @Override
    public void updateRuleSet(String packageName, RuleSet ruleSet) {
        StoreManager.getInstance().updateRuleSet(packageName, ruleSet);
    }

    @Override
    public void removeRuleSet(String packageName) {
        StoreManager.getInstance().removeRuleSet(packageName);
    }

    @Override
    public void updateSetting(String feature, boolean enabled) {
    }

    @Override
    public boolean getBooleanSetting(String feature) {
        return false;
    }
}
