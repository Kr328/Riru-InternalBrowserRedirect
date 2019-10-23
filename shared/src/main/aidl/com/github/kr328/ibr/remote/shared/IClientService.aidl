package com.github.kr328.ibr.remote.shared;

parcelable RuleSet;

interface IClientService {
    RuleSet queryRuleSet(String packageName);
}
