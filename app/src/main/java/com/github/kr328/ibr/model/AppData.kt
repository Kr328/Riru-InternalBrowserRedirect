package com.github.kr328.ibr.model

import android.graphics.drawable.Drawable
import com.github.kr328.ibr.remote.model.RuleSet

data class AppData(val packageName: String, val name: String, val version: String, val icon: Drawable, val ruleSet: RuleSet) {

}