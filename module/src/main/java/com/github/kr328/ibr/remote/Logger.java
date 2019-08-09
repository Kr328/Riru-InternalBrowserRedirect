package com.github.kr328.ibr.remote;

import android.content.Intent;
import android.os.Bundle;

import java.util.Collection;
import java.util.Map;

public class Logger {
    public static String log(String callingPackage, Intent intent) {
        return "Intent from " + callingPackage + "\n" +
                "  Action: " + intent.getAction() + '\n' +
                "  Category: " + intent.getCategories() + '\n' +
                "  Data: " + intent.getData() + '\n' +
                "  Package: " + intent.getPackage() + "\n" +
                "  Component: " + intent.getComponent() +  "\n" +
                "  Extra: " + log(intent.getExtras(), "    ");
    }

    public static String log(Bundle bundle, String padding) {
        if (bundle == null)
            return "null";

        StringBuilder sb = new StringBuilder();

        for (String key : bundle.keySet()) {
            Object object = bundle.get(key);

            sb.append('\n').append(padding).append(key).append(": ");

            if (object == null)
                sb.append("null").append('\n');
            else if (object instanceof Bundle)
                sb.append(log((Bundle) object, padding + "  "));
            else if (object instanceof Collection)
                sb.append(log((Collection) object, padding + "  "));
            else if (object instanceof Map)
                sb.append(log((Map) object, padding + "  "));
            else
                sb.append(object.toString());
        }

        return sb.toString();
    }

    public static String log(Collection collection, String padding) {
        if (collection == null)
            return "null";

        StringBuilder sb = new StringBuilder();

        for (Object object : collection) {
            sb.append('\n').append(padding).append("- ");

            if (object == null)
                sb.append("null").append('\n');
            else if (object instanceof Bundle)
                sb.append(log((Bundle) object, padding + "  "));
            else if (object instanceof Collection)
                sb.append(log((Collection) object, padding + "  "));
            else if (object instanceof Map)
                sb.append(log((Map) object, padding + "  "));
            else
                sb.append(object.toString());
        }

        return sb.toString();
    }

    public static String log(Map map, String padding) {
        if (map == null)
            return "null";

        StringBuilder sb = new StringBuilder();

        for (Object key : map.keySet()) {
            Object object = map.get(key);

            sb.append('\n').append(padding).append(key).append(": ");

            if (object == null)
                sb.append("null").append('\n');
            else if (object instanceof Bundle)
                sb.append(log((Bundle) object, padding + "  "));
            else if (object instanceof Collection)
                sb.append(log((Collection) object, padding + "  "));
            else if (object instanceof Map)
                sb.append(log((Map) object, padding + "  "));
            else
                sb.append(object.toString());
        }

        return sb.toString();
    }
}
