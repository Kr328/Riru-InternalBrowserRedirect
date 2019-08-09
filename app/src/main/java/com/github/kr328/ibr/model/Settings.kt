package com.github.kr328.ibr.model

import android.graphics.drawable.Drawable

interface Settings {
    data class HeaderSwitch(val enabled: Boolean,val state: Boolean, val title: String, val onCheckedChangeListener: (checked: Boolean) -> Unit = {}) : Settings
    data class AppInfo(val icon: Drawable, val name: String, val version: String, val packageName: String, val onInfoClicked: () -> Unit = {}) : Settings
    data class Button(val icon: Drawable?, val title: String ,val summary: String, val onClicked: () -> Unit = {}) : Settings
    data class Title(val title: String) : Settings
}