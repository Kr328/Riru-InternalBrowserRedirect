package com.github.kr328.ibr;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.Intent;
import android.util.Log;
import android.util.Singleton;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.PatternSyntaxException;

@SuppressWarnings("unused")
public class Injector {
    public static void inject(String configData) {
        if ( injected ) {
            Log.i(Constants.TAG, "Ignore Injected");
            return;
        }

        injected = true;

        Log.i(Constants.TAG, "Injecting");


    }

    private static boolean injected = false;
}
