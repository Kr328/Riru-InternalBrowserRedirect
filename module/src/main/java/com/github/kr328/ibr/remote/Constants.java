package com.github.kr328.ibr.remote;

import java.util.regex.Pattern;

public class Constants {
    // Application Basic
    public static final String TAG = "InternalBrowserRedirect";
    public static final String APPLICATION_ID = "com.github.kr328.ibr";

    // Service
    public static final int VERSION = 5;

    // Data Store
    public static final String  DATA_STORE_DIRECTORY = "/data/misc/riru/modules/internal_browser_redirect/userdata/";
    public static final Pattern PATTERN_CONFIG_FILE = Pattern.compile("rules.([a-zA-Z.]+).json");
    public static final String  TEMPLATE_CONFIG_FILE_NAME = "rules.%s.json";

    // Service Transact
    public static final int ACTIVITY_CONNECT_SERVER_CODE = 24519;
    public static final int ACTIVITY_CONNECT_CLIENT_CODE = 24520;

    // Service Status
    public static final String SERVICE_STATUE_KEY = "sys.ibr.status";

    // Intent
    public static final String INTENT_CATEGORY_IGNORE = "com.github.kr328.intent.ibr.category.IGNORE";
}
