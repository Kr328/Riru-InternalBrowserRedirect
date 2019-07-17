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
        RuleSet ruleSet = StoreManager.getInstance().getRuleSet(callingPackage);
        if ( ruleSet != null ) {
            RuleSetMatcher.Result result = RuleSetMatcher.matches(ruleSet, intent);
            if ( result.matches ) {
                Log.i(Constants.TAG, "Rule " + result.ruleSetTag + "|" + result.ruleTag + " matches " + intent);

                intent = Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(result.uri), "打开浏览器");
                intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{new LabeledIntent(intent, callingPackage, "使用内部浏览器", 0)});
            }
        }

        return original.startActivity(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);
    }
}
