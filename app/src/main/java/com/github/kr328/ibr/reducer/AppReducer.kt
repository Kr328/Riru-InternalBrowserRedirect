package com.github.kr328.ibr.reducer

import com.github.kr328.ibr.state.AppState
import org.rekotlin.Action

object AppReducer {
    fun handle(action: Action, appState: AppState?): AppState = AppState(
            appListState = AppListReducer.handle(action, appState?.appListState),
            editAppState = EditAppReducer.handle(action, appState?.editAppState)
    )
}