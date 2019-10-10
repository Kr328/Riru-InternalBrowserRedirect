package com.github.kr328.ibr.model

import android.graphics.drawable.Drawable

data class AppListElement(val packageName: String, val name: String, val appRuleState: AppRuleState, val icon: Drawable) {
    fun equalsBase(other: AppListElement) =
            packageName == other.packageName && name == other.name && appRuleState == other.appRuleState

    data class AppRuleState(val enabled: Boolean, val ruleType: RuleType)
    enum class RuleType {
        UNKNOWN, ONLINE, PRELOAD
    }
}
