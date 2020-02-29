package com.github.kr328.ibr.remote.i18n;

import android.app.ActivityThread;
import android.app.Application;

import java.util.Locale;

public class I18nFactory {
    private static I18n cachedI18n;

    public synchronized static I18n get() {
        if ( cachedI18n != null )
            return cachedI18n;

        Locale locale = Locale.getDefault();

        ActivityThread activityThread = ActivityThread.currentActivityThread();
        if ( activityThread != null ) {
            Application application = activityThread.getApplication();
            if ( application != null ) {
                Locale currentLocale = application.getResources().getConfiguration().getLocales().get(0);
                if ( currentLocale != null )
                    locale = currentLocale;
            }
        }

        String code = locale.getLanguage() + "-" + locale.getCountry();

        switch (code.toLowerCase(locale)) {
            case "zh-cn":
            case "zh-tw":
            case "zh-hk":
                cachedI18n = new ZhCN();
            default:
                cachedI18n = new I18n();
        }

        return cachedI18n;
    }
}
