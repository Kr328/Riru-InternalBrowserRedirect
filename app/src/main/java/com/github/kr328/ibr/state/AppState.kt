package com.github.kr328.ibr.state

import org.rekotlin.StateType

data class AppState(val appListState: AppListState? = null) : StateType