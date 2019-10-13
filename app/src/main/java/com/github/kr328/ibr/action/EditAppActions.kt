package com.github.kr328.ibr.action

import android.graphics.drawable.Drawable
import org.rekotlin.Action

data class EditAppCreatedActivityAction(val packageName: CharSequence) : Action

data class EditAppSetAppInfoAction(val packageName: CharSequence, val name: CharSequence, val version: CharSequence, val icon: Drawable) : Action