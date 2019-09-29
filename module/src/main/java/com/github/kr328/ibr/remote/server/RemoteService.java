package com.github.kr328.ibr.remote.server;

import android.content.pm.IPackageManager;
import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.model.RuleSet;

import java.util.Map;

public class RemoteService extends IRemoteService.Stub {
    static RemoteService INSTANCE = new RemoteService();

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
