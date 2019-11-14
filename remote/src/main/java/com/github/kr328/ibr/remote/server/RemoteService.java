package com.github.kr328.ibr.remote.server;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.content.pm.IPackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.shared.IRemoteService;
import com.github.kr328.ibr.remote.shared.RuleSet;
import com.github.kr328.ibr.remote.shared.SharedVersion;
import com.github.kr328.ibr.remote.utils.UserHandleUtils;

public class RemoteService extends IRemoteService.Stub {
    private IPackageManager packageManager;
    private IActivityManager activityManager;

    RemoteService() {
        StoreManager.getInstance().load();
        SystemProperties.set(Constants.LAST_UPDATE_KEY, String.valueOf(System.currentTimeMillis()));
    }

    private IPackageManager getPackageManager() {
        if (packageManager == null)
            packageManager = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
        return packageManager;
    }

    private IActivityManager getActivityManager() {
        if (activityManager == null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                activityManager = ActivityManagerNative.Stub.asInterface(ServiceManager.getService("activity"));
            else
                activityManager = IActivityManager.Stub.asInterface(ServiceManager.getService("activity"));
        }
        return activityManager;
    }

    private void enforcePermission() throws RemoteException {
        String[] packages = getPackageManager().getPackagesForUid(Binder.getCallingUid());
        for (String pkg : packages) {
            if (Constants.APPLICATION_ID.equals(pkg))
                return;
        }
        throw new RemoteException("Permission denied");
    }

    @Override
    public int getVersion() throws RemoteException {
        enforcePermission();

        return SharedVersion.VERSION_INT;
    }

    @Override
    public String[] queryEnabledPackages() throws RemoteException {
        enforcePermission();

        return StoreManager.getInstance().getRuleSets().keySet().toArray(new String[0]);
    }

    @Override
    public RuleSet queryRuleSet(String packageName) throws RemoteException {
        enforcePermission();

        return StoreManager.getInstance().getRuleSet(packageName);
    }

    @Override
    public void updateRuleSet(String packageName, RuleSet ruleSet) throws RemoteException {
        enforcePermission();

        if (StoreManager.getInstance().getRuleSet(packageName) == null) {
            long identity = Binder.clearCallingIdentity();
            getActivityManager().forceStopPackage(packageName, UserHandleUtils.getUserIdFromUid(Binder.getCallingUid()));
            Binder.restoreCallingIdentity(identity);
        }

        StoreManager.getInstance().updateRuleSet(packageName, ruleSet);

        SystemProperties.set(Constants.LAST_UPDATE_KEY, String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public void removeRuleSet(String packageName) throws RemoteException {
        enforcePermission();

        if (StoreManager.getInstance().getRuleSet(packageName) != null) {
            long identity = Binder.clearCallingIdentity();
            getActivityManager().forceStopPackage(packageName, UserHandleUtils.getUserIdFromUid(Binder.getCallingUid()));
            Binder.restoreCallingIdentity(identity);
        }

        StoreManager.getInstance().removeRuleSet(packageName);

        SystemProperties.set(Constants.LAST_UPDATE_KEY, String.valueOf(System.currentTimeMillis()));
    }
}
