package com.github.kr328.ibr.remote.utils;

public class UserHandleUtils {
    public static int getUserIdFromUid(int uid) {
        return uid / 100000;
    }
}
