package com.github.kr328.ibr.model

import android.graphics.drawable.Drawable

data class AppData(val packageName: String, val name: String, val version: String, val icon: Drawable, val onlineRuleSet: RuleSet?)