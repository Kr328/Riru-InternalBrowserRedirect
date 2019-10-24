package com.github.kr328.ibr.reducer

import com.github.kr328.ibr.action.*
import com.github.kr328.ibr.state.EditAppState
import org.rekotlin.Action

object EditAppReducer {
    fun handle(action: Action, state: EditAppState?): EditAppState? {
        var result = state

        when (action) {
            is EditAppSetAppInfoAction ->
                result = state?.copy(packageName = action.packageName, name = action.name, version = action.version, icon = action.icon)
                        ?: EditAppState(action.icon, action.name, action.packageName, action.version, false, null, false, null, false)
            is EditAppSetRuleSetAction -> {
                result = state?.copy(onlineRules = action.onlineRuleSet, localRules = action.localRuleSet)
            }
            is EditAppSetRuleSetEnabledAction -> {
                result = state?.copy(onlineEnable = action.onlineEnable, localEnable = action.localEnable)
            }
            is EditAppSetRefreshingAction -> {
                result = state?.copy(isRefreshing = action.refreshing)
            }
            is EditAppStoppedActivityAction -> {
                result = null
            }
        }

        return result
    }
}