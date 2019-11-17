package com.github.kr328.ibr.remote.i18n;

public class I18n {
    public final static String STRING_OPEN_LINK = "string_open_link";
    public final static String STRING_INTERNAL_BROWSER = "string_internal_browser";

    public String getString(String key) {
        switch (key) {
            case STRING_INTERNAL_BROWSER:
                return "Internal";
            case STRING_OPEN_LINK:
                return "Open Link...";
        }
        return key;
    }
}
