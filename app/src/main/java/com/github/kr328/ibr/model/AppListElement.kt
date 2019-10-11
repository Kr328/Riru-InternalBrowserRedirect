package com.github.kr328.ibr.model

import android.graphics.drawable.Drawable

data class AppListElement(val packageName: String, val name: String, val ruleStatus: Set<RuleStatus>, val icon: Drawable) {
    fun equalsBase(other: AppListElement) =
            packageName == other.packageName && name == other.name && ruleStatus == other.ruleStatus

    enum class RuleStatus {
        ENABLED, ONLINE, LOCAL
    }
}
