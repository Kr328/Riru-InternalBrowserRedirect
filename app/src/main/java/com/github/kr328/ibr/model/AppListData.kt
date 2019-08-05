package com.github.kr328.ibr.model

import android.graphics.drawable.Drawable

data class AppListData(val elements: List<Element>) {
    data class Element(val packageName: String, val name: String, val appState: AppState, val icon: Drawable) {
        fun equalsBase(other: Element) =
                packageName == other.packageName && name == other.name && appState == other.appState
    }

    data class AppState(val enabled: Boolean, val ruleType: RuleType)
    enum class RuleType {
        UNKNOWN, ONLINE, PRELOAD
    }
}
