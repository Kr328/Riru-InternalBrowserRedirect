package com.github.kr328.ibr.remote.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class RuleSetStore implements Parcelable {
    public Map<String, RuleSet> ruleSets = new HashMap<>();

    protected RuleSetStore(Parcel in) {
        in.readMap(ruleSets, RuleSetStore.class.getClassLoader());
    }

    public static final Creator<RuleSetStore> CREATOR = new Creator<RuleSetStore>() {
        @Override
        public RuleSetStore createFromParcel(Parcel in) {
            return new RuleSetStore(in);
        }

        @Override
        public RuleSetStore[] newArray(int size) {
            return new RuleSetStore[size];
        }
    };

    @Override
    public int describeContents() {
        return ruleSets.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(ruleSets);
    }
}
