package com.github.kr328.ibr.remote;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Binder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;

import com.github.kr328.ibr.remote.compat.ActivityManagerProxyFactory;
import com.github.kr328.ibr.remote.data.StoreManager;
import com.github.kr328.ibr.remote.i18n.I18n;
import com.github.kr328.ibr.remote.i18n.I18nFactory;
import com.github.kr328.ibr.remote.model.RuleSet;

public class ActivityManagerProxy implements ActivityManagerProxyFactory.Callback {
    @Override
    public Intent startActivity(String callingPackage, Intent intent) {
        if (intent.getComponent() == null)
            return intent;

        if (StoreManager.getInstance().isDebugModeEnabled()) {
            for (String line : Logger.log(callingPackage, (Intent) intent.clone()).split("\n"))
                Log.d(Constants.TAG, line);
        }

        if (intent.hasCategory(Constants.INTENT_CATEGORY_IGNORE))
            return intent;

        RuleSet ruleSet = StoreManager.getInstance().getRuleSet(callingPackage);
        if (ruleSet != null) {
            Intent clonedIntent = (Intent) intent.clone();
            RuleSetMatcher.Result result = RuleSetMatcher.matches(ruleSet, clonedIntent);

            if (result.matches) {
                Log.i(Constants.TAG, "Rule " + result.ruleSetTag + "|" + result.ruleTag + " matches " + result.uri);

                I18n i18n = I18nFactory.get();

                Intent chooser = Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(result.uri), i18n.getString(I18n.STRING_OPEN_LINK));

                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{new LabeledIntent(intent.addCategory(Constants.INTENT_CATEGORY_IGNORE),
                        callingPackage, i18n.getString(I18n.STRING_INTERNAL_BROWSER), 0)});

                return chooser;
            }
        }

        return intent;
    }

    @Override
    public Binder queryRedirectService() throws RemoteException {
        SystemProperties.set(Constants.SERVICE_STATUE_KEY, "running");

        RemoteService.INSTANCE.enforcePermission();

        return RemoteService.INSTANCE;
    }
}
