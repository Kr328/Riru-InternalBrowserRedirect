package com.github.kr328.ibr.reducer

import android.provider.SyncStateContract
import android.util.Log
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.action.EditAppSetAppInfoAction
import com.github.kr328.ibr.action.EditAppSetRefreshingAction
import com.github.kr328.ibr.action.EditAppSetRuleSetAction
import com.github.kr328.ibr.state.EditAppState
import org.rekotlin.Action

object EditAppReducer {
    fun handle(action: Action, state: EditAppState?): EditAppState? {
        var result = state

        when (action) {
            is EditAppSetAppInfoAction ->
                result = state?.copy(packageName = action.packageName, name = action.name, version = action.version, icon = action.icon)
                        ?: EditAppState(action.icon, action.name, action.packageName, action.version, false, null,false, null)
            is EditAppSetRuleSetAction -> {
                result = state?.copy(onlineRules = action.onlineRuleSet, localRules = action.localRuleSet)
            }
            is EditAppSetRefreshingAction -> {
                result = state?.copy(isRefreshing = action.refreshing)
            }
        }

        return result
    }
}