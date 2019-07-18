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

import com.github.kr328.ibr.remote.model.RuleSet;
import com.github.kr328.ibr.remote.proxy.ProxyBinderFactory.CustomTransact;
import com.github.kr328.ibr.remote.proxy.ProxyBinderFactory.ReplaceTransact;

import java.util.Objects;

@CustomTransact({Constants.ACTIVITY_CONNECT_TRANSACT_CODE})
public class ActivityManagerProxy extends IActivityManager.Stub {
    private IActivityManager original;

    ActivityManagerProxy(IActivityManager original) {
        this.original = original;
    }

    @Override
    @ReplaceTransact
    public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
        Intent clonedIntent = new Intent(intent);

        if (Intent.ACTION_VIEW.equals(clonedIntent.getAction()) && clonedIntent.getComponent() == null && clonedIntent.getPackage() == null)
            return original.startActivity(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);
        if (clonedIntent.hasExtra(Constants.INTENT_EXTRA_IBR_ORIGINAL_EXTRAS))
            return original.startActivity(caller, callingPackage,
                    intent.replaceExtras(Objects.requireNonNull(clonedIntent.getBundleExtra(Constants.INTENT_EXTRA_IBR_ORIGINAL_EXTRAS))) ,
                    resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);

        RuleSet ruleSet = StoreManager.getInstance().getRuleSet(callingPackage);
        if (ruleSet != null) {
            RuleSetMatcher.Result result = RuleSetMatcher.matches(ruleSet, clonedIntent);
            if (result.matches) {
                Log.i(Constants.TAG, "Rule " + result.ruleSetTag + "|" + result.ruleTag + " matches " + result.uri);

                Intent chooser = Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(result.uri), "\u6253\u5f00\u94fe\u63a5");
                Bundle originalExtras = intent.getExtras();

                intent.replaceExtras(new Bundle()).putExtra(Constants.INTENT_EXTRA_IBR_ORIGINAL_EXTRAS, originalExtras);
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{new LabeledIntent(intent, callingPackage, "\u5185\u90e8\u6d4f\u89c8\u5668", 0)});

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
