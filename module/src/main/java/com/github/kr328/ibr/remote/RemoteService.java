package com.github.kr328.ibr.remote;

import android.content.pm.IPackageManager;
import android.os.Binder;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.github.kr328.ibr.remote.model.RuleSet;

import java.util.Map;

public class RemoteService extends IRemoteService.Stub {
    public static RemoteService INSTANCE = new RemoteService();

    private IPackageManager packageManager;
    private IPackageManager getPackageManager() {
        if ( packageManager == null )
            packageManager = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
        return packageManager;
    }

    void enforcePermission() throws RemoteException {
        String[] pkgs = getPackageManager().getPackagesForUid(Binder.getCallingUid());
        for ( String pkg : pkgs ) {
            if ( Constants.APPLICATION_ID.equals(pkg) )
                return;
        }
        throw new RemoteException("Permission denied");
    }

    @Override
    public int getVersion() throws RemoteException {
        return Constants.VERSION;
    }

    @Override
    public Map queryAllRuleSet() throws RemoteException {
        return StoreManager.getInstance().getRuleSets();
    }

    @Override
    public RuleSet queryRuleSet(String packageName) throws RemoteException {
        return StoreManager.getInstance().getRuleSet(packageName);
    }

    @Override
    public void updateRuleSet(String packageName, RuleSet ruleSet) throws RemoteException {
        StoreManager.getInstance().updateRuleSet(packageName, ruleSet);
    }

    @Override
    public void remoteRuleSet(String packageName) throws RemoteException {
        StoreManager.getInstance().updateRuleSet(packageName, new RuleSet());
    }
}
