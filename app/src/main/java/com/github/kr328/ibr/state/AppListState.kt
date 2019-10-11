package com.github.kr328.ibr.state

import com.github.kr328.ibr.model.AppListElement
import org.rekotlin.StateType

data class AppListState(val list: List<AppListElement> = emptyList(), val progress: Boolean = false) : StateType