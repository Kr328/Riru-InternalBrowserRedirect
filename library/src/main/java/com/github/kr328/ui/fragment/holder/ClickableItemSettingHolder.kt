package com.github.kr328.ui.fragment.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView

data class ClickableItemSettingHolder(override val id: String, override val type: SettingHolder.Type = SettingHolder.Type.CLIAKABLE_ITEM,
                                      val icon: ImageView,
                                      val clickable: View,
                                      val title: TextView,
                                      val summary: TextView) : SettingHolder