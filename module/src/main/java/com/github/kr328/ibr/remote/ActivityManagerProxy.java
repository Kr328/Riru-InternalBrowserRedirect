package com.github.kr328.ibr.remote;

import android.app.IActivityManager;
import android.app.IApplicationThread;
import android.app.ProfilerInfo;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import com.github.kr328.ibr.remote.data.StoreManager;
import com.github.kr328.ibr.remote.i18n.I18n;
import com.github.kr328.ibr.remote.i18n.I18nFactory;
import com.github.kr328.ibr.remote.model.RuleSet;
import com.github.kr328.ibr.remote.proxy.ProxyBinderFactory.CustomTransact;
import com.github.kr328.ibr.remote.proxy.ProxyBinderFactory.ReplaceTransact;

@CustomTransact({Constants.ACTIVITY_CONNECT_TRANSACT_CODE})
public class ActivityManagerProxy extends IActivityManager.Stub {
    private IActivityManager original;

    ActivityManagerProxy(IActivityManager original) {
        this.original = original;
    }

    @Override
    @ReplaceTransact
    public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
        if ( intent.getComponent() == null )
            return original.startActivity(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);

        if ( StoreManager.getInstance().isDebugModeEnabled() ) {
            for ( String line : Logger.log(new Intent(intent)).split("\n") )
                Log.d(Constants.TAG, line);
        }

        if ( intent.hasCategory(Constants.INTENT_CATEGORY_IGNORE) )
            return original.startActivity(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);

        RuleSet ruleSet = StoreManager.getInstance().getRuleSet(callingPackage);
        if (ruleSet != null) {
            Intent clonedIntent = new Intent(intent);
            RuleSetMatcher.Result result = RuleSetMatcher.matches(ruleSet, clonedIntent);

            if (result.matches) {
                Log.i(Constants.TAG, "Rule " + result.ruleSetTag + "|" + result.ruleTag + " matches " + result.uri);

                I18n i18n = I18nFactory.get();

                Intent chooser = Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(result.uri), i18n.getString(I18n.STRING_OPEN_LINK));

                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{new LabeledIntent(intent.addCategory(Constants.INTENT_CATEGORY_IGNORE),
                        callingPackage, i18n.getString(I18n.STRING_INTERNAL_BROWSER), 0)});

                return original.startActivity(caller, callingPackage, chooser, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);
            }
        }

        return original.startActivity(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        if ( code == Constants.ACTIVITY_CONNECT_TRANSACT_CODE ) {
            RemoteService.INSTANCE.enforcePermission();
            data.enforceInterface(Constants.APPLICATION_ID);

            reply.writeStrongBinder(RemoteService.INSTANCE);

            return true;
        }

        return super.onTransact(code, data, reply, flags);
    }
}
