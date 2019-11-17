package com.github.kr328.ibr.remote.client;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.kr328.ibr.remote.shared.Rule;
import com.github.kr328.ibr.remote.shared.RuleSet;

import java.util.Collection;
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
            if (!"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme()) && !"content".equals(uri.getScheme()))
                continue;
            if (filterUri(uri, rule))
                continue;
            return new Result(ruleSet.tag, rule.tag, uri);
        }
        return null;
    }

    private static Uri parseIntentUrl(Intent intent, Uri uri) {
        if (!"intent".equals(uri.getScheme()))
            return Uri.EMPTY;

        Object baseObject;

        switch (optional(uri.getHost(), "")) {
            case "action":
                baseObject = intent.getAction();
                break;
            case "category":
                baseObject = intent.getCategories();
                break;
            case "data":
                baseObject = intent.getData();
                break;
            case "extras":
            case "extra":
                baseObject = intent.getExtras();
                break;
            default:
                return Uri.EMPTY;
        }

        for (String path : optional(uri.getPathSegments(), Collections.<String>emptyList())) {
            Uri data;

            if (baseObject == null )
                return Uri.EMPTY;
            else if ( baseObject instanceof Collection )
                baseObject = parseUriFromCollection((Collection) baseObject);
            else if (baseObject instanceof Bundle)
                baseObject = ((Bundle) baseObject).get(path);
            else if (baseObject instanceof Map)
                baseObject = ((Map) baseObject).get(path);
            else if ( baseObject instanceof Uri )
                baseObject = ((Uri) baseObject).getQueryParameter(path);
            else if ((data = Uri.parse(baseObject.toString())) != null)
                baseObject = data.getQueryParameter(path);
            else {
                baseObject = null;
                break;
            }
        }

        return Uri.parse(optional(baseObject, "").toString());
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

    private static Uri parseUriFromCollection(Collection collection) {
        for ( Object o : collection ) {
            Uri u = Uri.parse(o.toString());

            if ( u != null )
                return u;
        }

        return Uri.EMPTY;
    }

    static class Result {
        String ruleSetTag;
        String ruleTag;
        Uri uri;

        Result(String ruleSetTag, String ruleTag, Uri uri) {
            this.ruleSetTag = ruleSetTag;
            this.ruleTag = ruleTag;
            this.uri = uri;
        }
    }
}
