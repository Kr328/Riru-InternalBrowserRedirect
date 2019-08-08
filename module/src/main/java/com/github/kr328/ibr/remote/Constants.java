package com.github.kr328.ibr.remote;

import java.util.regex.Pattern;

public class Constants {
    public static final String TAG = "InternalBrowserRedirect";
    public static final String APPLICATION_ID = "com.github.kr328.ibr";

    static final int VERSION = 4;

    static final String SERVICE_STATUE_KEY = "sys.ibr.status";

    public static final String DATA_STORE_DIRECTORY = "/data/misc/riru/modules/internal_browser_redirect/userdata/";
    public static final Pattern PATTERN_CONFIG_FILE = Pattern.compile("rules.([a-zA-Z.]+).json");
    public static final String TEMPLATE_CONFIG_FILE = "rules.%s.json";

    static final String INTENT_CATEGORY_IGNORE = "com.github.kr328.intent.ibr.category.ignore";

    public static final int ACTIVITY_CONNECT_TRANSACT_CODE = 24519;

    static final String SETTING_DEBUG_MODE = "setting_debug_mode";
}
