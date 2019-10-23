package com.github.kr328.ibr.remote.server;

import android.content.pm.IPackageManager;
import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.shared.IRemoteService;
import com.github.kr328.ibr.remote.shared.RuleSet;

public class RemoteService extends IRemoteService.Stub {
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

        data.enforceInterface(IRemoteService.class.getName());

        enforcePermission();

        reply.writeStrongBinder(this);

        return true;
    }

    RemoteService() {
        StoreManager.getInstance().load();
        SystemProperties.set(Constants.LAST_UPDATE_KEY, String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public int getVersion() {
        return Constants.VERSION;
    }

    @Override
    public String[] queryEnabledPackages() {
        return StoreManager.getInstance().getRuleSets().keySet().toArray(new String[0]);
    }

    @Override
    public RuleSet queryRuleSet(String packageName) throws RemoteException {
        return StoreManager.getInstance().getRuleSet(packageName);
    }

    @Override
    public void updateRuleSet(String packageName, RuleSet ruleSet) throws RemoteException {
        StoreManager.getInstance().updateRuleSet(packageName, ruleSet);
        SystemProperties.set(Constants.LAST_UPDATE_KEY, String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public void removeRuleSet(String packageName) throws RemoteException {
        StoreManager.getInstance().removeRuleSet(packageName);
        SystemProperties.set(Constants.LAST_UPDATE_KEY, String.valueOf(System.currentTimeMillis()));
    }
}
