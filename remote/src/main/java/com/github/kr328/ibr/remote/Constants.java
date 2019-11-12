package com.github.kr328.ibr.remote;

import java.util.regex.Pattern;

public class Constants {
    // Application Basic
    public static final String TAG = "InternalBrowserRedirect";
    public static final String APPLICATION_ID = "com.github.kr328.ibr";

    // Data Store
    public static final String DATA_STORE_DIRECTORY = "/data/misc/riru/modules/internal_browser_redirect/userdata/";
    public static final Pattern PATTERN_CONFIG_FILE = Pattern.compile("rules.([a-zA-Z.]+).json");
    public static final String TEMPLATE_CONFIG_FILE_NAME = "rules.%s.json";

    // Client Service
    public static final String CLIENT_SERVICE_NAME = "internal_browser_redirect_client";

    // Service Status
    public static final String SERVICE_STATUE_KEY = "sys.ibr.status";
    public static final String LAST_UPDATE_KEY = "sys.ibr.last_update";

    // Intent
    public static final String INTENT_CATEGORY_IGNORE = "com.github.kr328.intent.ibr.category.IGNORE";
}
