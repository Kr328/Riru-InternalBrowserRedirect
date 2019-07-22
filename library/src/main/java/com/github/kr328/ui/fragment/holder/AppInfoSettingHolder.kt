package com.github.kr328.ui.fragment.holder

import android.widget.ImageView
import android.widget.TextView

data class AppInfoSettingHolder(override val id: String,
                           override val type: SettingHolder.Type = SettingHolder.Type.APP_INFO,
                           val icon: ImageView,
                           val name: TextView,
                           val version: TextView,
                           val packageName: TextView) : SettingHolder