package com.github.kr328.ibr.reducer

import com.github.kr328.ibr.action.AppListProgressAction
import com.github.kr328.ibr.action.AppListUpdatedAction
import com.github.kr328.ibr.state.AppListState
import org.rekotlin.Action

object AppListReducer {
    fun handle(action: Action, appListState: AppListState?): AppListState {
        var state = appListState ?: AppListState()

        when (action) {
            is AppListUpdatedAction -> state = state.copy(list = action.list)
            is AppListProgressAction -> state = state.copy(progress = action.show)
        }

        return state
    }
}