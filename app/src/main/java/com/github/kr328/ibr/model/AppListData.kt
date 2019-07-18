package com.github.kr328.ibr.model

import android.content.Context
import android.graphics.drawable.Drawable

data class AppListData(val elements: List<Element>) {
    data class Element(val packageName: String, val name: String, val appState: AppState, val icon: Drawable)
    data class AppState(val enabled: Boolean, val ruleType: Int) {
        companion object {
            const val RULE_TYPE_UNKNOWN = 0
            const val RULE_TYPE_ONLINE = 1
            const val RULE_TYPE_LOCAL = 2
        }
    }
}