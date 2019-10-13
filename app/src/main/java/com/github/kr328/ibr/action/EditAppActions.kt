package com.github.kr328.ibr.action

import android.graphics.drawable.Drawable
import com.github.kr328.ibr.model.StoreRuleSet
import org.rekotlin.Action

data class EditAppCreatedActivityAction(val packageName: String) : Action
data class EditAppStartedActivityAction(val packageName: String) : Action

data class EditAppRefreshAction(val packageName: String) : Action

data class EditAppSetAppInfoAction(val packageName: CharSequence, val name: CharSequence, val version: CharSequence, val icon: Drawable) : Action
data class EditAppSetRuleSetAction(val onlineRuleSet: StoreRuleSet?, val localRuleSet: StoreRuleSet?) : Action
data class EditAppSetRefreshingAction(val refreshing: Boolean) : Action