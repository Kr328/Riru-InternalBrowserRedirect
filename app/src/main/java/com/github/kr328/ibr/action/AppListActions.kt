package com.github.kr328.ibr.action

import com.github.kr328.ibr.model.AppListElement
import org.rekotlin.Action

enum class ALUError {
    LOAD_FAILURE
}

data class AppListUpdateErrorAction(val error: ALUError) : Action

class AppListStartedAction : Action
class AppListRefreshAction : Action

data class AppListProgressAction(val show: Boolean) : Action

data class AppListUpdatedAction(val list: List<AppListElement>) : Action
