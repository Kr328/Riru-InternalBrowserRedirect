package com.github.kr328.ibr.remote.shared;

parcelable Rule;
parcelable RuleSet;
parcelable RuleSetStore;

interface IRemoteService {
    int getVersion();
    String[] queryEnabledPackages();
    void updateRuleSet(String packageName, String namespace, RuleSet ruleSet);
    void removeRuleSet(String packageName, String namespace);
}
