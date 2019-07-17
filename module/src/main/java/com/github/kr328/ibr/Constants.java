package com.github.kr328.ibr;

import java.util.regex.Pattern;

public class Constants {
    public static final String TAG = "InternalBrowserRedirect";
    public static final String DATA_STORE_DIRECTORY = "/data/misc/riru/modules/internal_browser_redirect/userdata/";
    public static final Pattern PATTERN_CONFIG_FILE = Pattern.compile("config.([a-zA-Z.]+).json");
    public static final String TEMPLATE_CONFIG_FILE = "config.%s.json";
    public static final String INTENT_EXTRA_IBR_IGNORE = "com.github.kr328.intent.ibr.IGNORE";
}
