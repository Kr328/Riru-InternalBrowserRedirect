package com.github.kr328.ibr.remote.i18n;

public class ZhCN extends I18n {
    @Override
    public String getString(String key) {
        switch (key) {
            case STRING_INTERNAL_BROWSER:
                return "\u5185\u90e8\u6d4f\u89c8\u5668";
            case STRING_OPEN_LINK:
                return "\u6253\u5f00\u94fe\u63a5";
        }
        return key;
    }
}
