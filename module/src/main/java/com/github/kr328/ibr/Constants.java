package com.github.kr328.ibr;

import java.util.regex.Pattern;

public class Constants {
    public static final String TAG = "InternalBrowserRedirect";
    public static final String DATA_STORE_DIRECTORY = "/data/misc/riru/modules/internal_browser_redirect/userdata/";
    public static final Pattern PATTERN_CONFIG_FILE = Pattern.compile("rules.([a-zA-Z.]+).json");
    public static final String TEMPLATE_CONFIG_FILE = "rules.%s.json";
    public static final String INTENT_EXTRA_IBR_ORIGINAL_EXTRAS = "com.github.kr328.intent.ibr.ORIGINAL_EXTRAS";
    public static final int ACTIVITY_CONNECT_TRANSACT_CODE = 24519;
}
