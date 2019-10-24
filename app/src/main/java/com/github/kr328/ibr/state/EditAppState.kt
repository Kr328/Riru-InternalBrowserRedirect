package com.github.kr328.ibr.state

import android.graphics.drawable.Drawable
import com.github.kr328.ibr.model.StoreRuleSet
import org.rekotlin.StateType

data class EditAppState(val icon: Drawable, val name: CharSequence, val packageName: CharSequence, val version: CharSequence,
                        val onlineEnable: Boolean, val onlineRules: StoreRuleSet?,
                        val localEnable: Boolean, val localRules: StoreRuleSet?,
                        val debug: Boolean,
                        val isRefreshing: Boolean = false) : StateType
