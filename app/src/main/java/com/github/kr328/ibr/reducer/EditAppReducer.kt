package com.github.kr328.ibr.reducer

import com.github.kr328.ibr.action.EditAppSetApplicationInfo
import com.github.kr328.ibr.state.EditAppState
import org.rekotlin.Action

object EditAppReducer {
    fun handle(action: Action, state: EditAppState?): EditAppState? {
        var result = state

        when (action) {
            is EditAppSetApplicationInfo ->
                result = state?.copy(packageName = action.packageName, name = action.name, version = action.version, icon = action.icon) ?:
                        EditAppState(action.icon, action.name, action.packageName, action.version, false, null, false, null)

        }

        return result
    }
}