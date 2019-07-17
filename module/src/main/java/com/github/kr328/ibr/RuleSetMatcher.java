package com.github.kr328.ibr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.github.kr328.ibr.model.Rule;
import com.github.kr328.ibr.model.RuleSet;

import java.util.Collections;
import java.util.Map;

class RuleSetMatcher {
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

    static Result matches(RuleSet ruleSet, Intent intent) {
        for (Rule rule : ruleSet.getRules()) {
            Uri uri = parseIntentUrl(intent, rule.getUrlPath());
            if ( uri == null )
                continue;
            if ( uri.equals(Uri.EMPTY) )
                continue;
            if ( !"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme()) )
                continue;
            return new Result(true, ruleSet.getTag(), rule.getTag(), uri);
        }
        return new Result();
    }

    private static Uri parseIntentUrl(Intent intent, Uri uri) {
        if ( !"intent".equals(uri.getScheme()) )
            return Uri.EMPTY;

        switch (optional(uri.getHost(), "")) {
            case "action":
                return Uri.parse(optional(intent.getAction(), ""));
            case "category":
                for ( String cat : optional(intent.getCategories(), Collections.<String>emptySet()) ) {
                    Uri u = Uri.parse(cat);
                    if ( "http".equals(u.getScheme()) || "https".equals(u.getScheme()) )
                        return u;
                }
                return Uri.EMPTY;
            case "data":
                return optional(intent.getData(), Uri.EMPTY);
            case "extra":
                Object nextObject = intent.getExtras();
                for ( String path : optional(uri.getPathSegments(), Collections.<String>emptyList()) ) {
                    if ( nextObject instanceof Bundle)
                        nextObject = ((Bundle)nextObject).get(path);
                    else if ( nextObject instanceof Map )
                        nextObject = ((Map)nextObject).get(path);
                    else {
                        nextObject = "";
                        break;
                    }
                }
                return Uri.parse(optional(nextObject, "").toString());
            default:
                return Uri.EMPTY;
        }
    }

    private static <T> T optional(T o, T d) {
        if ( o == null )
            return d;
        return o;
    }
}
