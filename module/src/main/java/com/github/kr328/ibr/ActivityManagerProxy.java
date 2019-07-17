package com.github.kr328.ibr;

import android.app.IActivityManager;
import android.app.IApplicationThread;
import android.app.ProfilerInfo;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import com.github.kr328.ibr.model.RuleSet;
import com.github.kr328.ibr.proxy.ProxyBinderFactory.ReplaceTransact;

public class ActivityManagerProxy extends IActivityManager.Stub {
    private IActivityManager original;

    ActivityManagerProxy(IActivityManager original) {
        this.original = original;
    }

    @Override
    @ReplaceTransact
    public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
        if ( Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getComponent() == null && intent.getPackage() == null )
            return original.startActivity(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);
        if ( intent.getBooleanExtra(Constants.INTENT_EXTRA_IBR_IGNORE, false) )
            return original.startActivity(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);

        RuleSet ruleSet = StoreManager.getInstance().getRuleSet(callingPackage);
        if ( ruleSet != null ) {
            RuleSetMatcher.Result result = RuleSetMatcher.matches(ruleSet, intent);
            if ( result.matches ) {
                Log.i(Constants.TAG, "Rule " + result.ruleSetTag + "|" + result.ruleTag + " matches " + result.uri);

                Intent chooser = Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(result.uri), "\u6253\u5f00\u94fe\u63a5");
                intent.putExtra(Constants.INTENT_EXTRA_IBR_IGNORE, true);
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{new LabeledIntent(intent, callingPackage, "\u5185\u90e8\u6d4f\u89c8\u5668", 0)});
                intent = chooser;
            }
        }

        return original.startActivity(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);
    }
}
