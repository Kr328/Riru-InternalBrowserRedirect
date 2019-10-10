package com.github.kr328.ibr.action

import com.github.kr328.ibr.model.AppListElement
import org.rekotlin.Action

data class UpdateAppListAction(val list: List<AppListElement>) : Action