package com.github.kr328.ibr.remote.i18n;

import android.os.SystemProperties;

public class I18nFactory {
    private static ZhCN zhCN = new ZhCN();
    private static I18n fallback = new I18n();

    public static I18n get() {
        switch (SystemProperties.get("persist.sys.locale", "en-US")) {
            case "zh-CN":
            case "zh-TW":
            case "zh-HK":
                return zhCN;
            default:
                return fallback;
        }
    }
}
