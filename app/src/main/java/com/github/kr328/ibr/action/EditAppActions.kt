package com.github.kr328.ibr.action

import android.graphics.drawable.Drawable
import org.rekotlin.Action

data class EditAppSetApplicationInfo(val packageName: CharSequence, val version: CharSequence, val name: CharSequence, val icon: Drawable) : Action