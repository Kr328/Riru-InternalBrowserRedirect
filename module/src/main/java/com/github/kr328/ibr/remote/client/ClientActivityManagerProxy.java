package com.github.kr328.ibr.remote.client;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import com.github.kr328.ibr.remote.Constants;
import com.github.kr328.ibr.remote.i18n.I18n;
import com.github.kr328.ibr.remote.i18n.I18nFactory;
import com.github.kr328.ibr.remote.model.RuleSet;

public class ClientActivityManagerProxy extends BaseClientActivityManagerProxy {
    @Override
    protected void handleStartActivity(StartActivityPayloads payloads) throws RemoteException {
        if (payloads.intent.getComponent() == null)
            return;

        if (payloads.intent.getCategories().contains(Constants.INTENT_CATEGORY_IGNORE))
            return;

        RuleSet ruleSet = ClientConnection.getConnection().queryRuleSet(payloads.callingPackage);

        if (ruleSet.debug) {
            for (String line : Logger.log(payloads.callingPackage, payloads.intent).split("\n"))
                Log.d(Constants.TAG, line);
        }

        RuleSetMatcher.Result result = RuleSetMatcher.matches(ruleSet, payloads.intent);

        if (result.matches) {
            Log.i(Constants.TAG, "Rule " + result.ruleSetTag + "|" + result.ruleTag + " matches " + result.uri);

            I18n i18n = I18nFactory.get();

            Intent chooser = Intent.createChooser(new Intent(Intent.ACTION_VIEW).setData(result.uri),
                    i18n.getString(I18n.STRING_OPEN_LINK));

            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{
                    new LabeledIntent(payloads.intent.addCategory(Constants.INTENT_CATEGORY_IGNORE),
                            payloads.callingPackage, i18n.getString(I18n.STRING_INTERNAL_BROWSER), 0)});

            payloads.intent = chooser;
            payloads.options = null;
        }
    }

    void onInitialized() {
        ClientConnection.openConnection();
    }
}
