package com.github.kr328.ibr.reducer

import com.github.kr328.ibr.action.UpdateAppListAction
import com.github.kr328.ibr.state.AppListState
import org.rekotlin.Action

object AppListReducer {
    fun handle(action: Action, appListState: AppListState?): AppListState {
        var state = appListState ?: AppListState(emptyList())

        when ( action ) {
            is UpdateAppListAction -> state = state.copy(list = action.list)
        }

        return state
    }
}