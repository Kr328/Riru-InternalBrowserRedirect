package com.github.kr328.ibr.remote;

import java.util.regex.Pattern;

public class Constants {
    // Application Basic
    public static final String TAG = "InternalBrowserRedirect";
    public static final String APPLICATION_ID = "com.github.kr328.ibr";

    // Data Store
    public static final String DATA_STORE_DIRECTORY = "/data/misc/internal_browser_redirect/userdata/";
    public static final Pattern PATTERN_CONFIG_FILE = Pattern.compile("rules.([a-zA-Z.]+).json");
    public static final String TEMPLATE_CONFIG_FILE_NAME = "rules.%s.json";

    public static final String LAST_UPDATE_KEY = "sys.ibr.last_update";

    // Intent
    public static final String INTENT_CATEGORY_IGNORE = "com.github.kr328.intent.ibr.category.IGNORE";
}
