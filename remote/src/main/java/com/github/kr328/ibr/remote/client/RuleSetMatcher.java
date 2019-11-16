package com.github.kr328.ibr.remote.client;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.kr328.ibr.remote.shared.Rule;
import com.github.kr328.ibr.remote.shared.RuleSet;

import java.util.Collections;
import java.util.Map;

class RuleSetMatcher {
    static Result matches(RuleSet ruleSet, Intent intent) {
        for (Rule rule : ruleSet.rules) {
            Uri uri = parseIntentUrl(intent, rule.urlPath);
            if (uri == null)
                continue;
            if (uri == Uri.EMPTY)
                continue;
            if (!"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme()))
                continue;
            if (filterUri(uri, rule))
                continue;
            return new Result(true, ruleSet.tag, rule.tag, uri);
        }
        return new Result();
    }

    private static Uri parseIntentUrl(Intent intent, Uri uri) {
        if (!"intent".equals(uri.getScheme()))
            return Uri.EMPTY;

        switch (optional(uri.getHost(), "")) {
            case "action":
                return Uri.parse(optional(intent.getAction(), ""));
            case "category":
                for (String cat : optional(intent.getCategories(), Collections.<String>emptySet())) {
                    Uri u = Uri.parse(cat);
                    if ("http".equals(u.getScheme()) || "https".equals(u.getScheme()))
                        return u;
                }
                return Uri.EMPTY;
            case "data":
                return optional(intent.getData(), Uri.EMPTY);
            case "extras":
            case "extra":
                Object nextObject = intent.getExtras();
                for (String path : optional(uri.getPathSegments(), Collections.<String>emptyList())) {
                    Uri data;

                    if (nextObject instanceof Bundle)
                        nextObject = ((Bundle) nextObject).get(path);
                    else if (nextObject instanceof Map)
                        nextObject = ((Map) nextObject).get(path);
                    else if ((data = Uri.parse(optional(nextObject, "").toString())) != null)
                        nextObject = data.getQueryParameter(path);
                    else {
                        nextObject = null;
                        break;
                    }
                }
                return Uri.parse(optional(nextObject, "").toString());
            default:
                return Uri.EMPTY;
        }
    }

    private static boolean filterUri(Uri uri, Rule rule) {
        String url = uri.toString();

        return !url.matches(rule.regexForce) && url.matches(rule.regexIgnore);
    }

    private static <T> T optional(T o, T d) {
        if (o == null)
            return d;
        return o;
    }

    static class Result {
        boolean matches;
        String ruleSetTag;
        String ruleTag;
        Uri uri;

        Result(boolean matches, String ruleSetTag, String ruleTag, Uri uri) {
            this.matches = matches;
            this.ruleSetTag = ruleSetTag;
            this.ruleTag = ruleTag;
            this.uri = uri;
        }

        Result() {
        }
    }
}
