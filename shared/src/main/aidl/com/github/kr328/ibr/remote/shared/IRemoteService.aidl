package com.github.kr328.ibr.remote.shared;

parcelable Rule;
parcelable RuleSet;

interface IRemoteService {
    int getVersion();
    String[] queryEnabledPackages();
    RuleSet queryRuleSet(String packageName);
    void updateRuleSet(String packageName, in RuleSet ruleSet);
    void removeRuleSet(String packageName);
}
