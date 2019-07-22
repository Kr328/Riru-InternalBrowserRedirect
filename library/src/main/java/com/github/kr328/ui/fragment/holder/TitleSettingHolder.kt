package com.github.kr328.ui.fragment.holder

import android.widget.TextView

data class TitleSettingHolder(override val id: String, override val type: SettingHolder.Type = SettingHolder.Type.TITLE,
                              val title: TextView) : SettingHolder