package com.github.kr328.ibr.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.github.kr328.ibr.R

class SettingAppInfo @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
        FrameLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    var icon: Drawable
        get() = iconView.drawable
        set(value) { iconView.setImageDrawable(value) }
    var name: CharSequence
        get() = nameView.text
        set(value) { nameView.text = value }
    var packageName: CharSequence
        get() = packageView.text
        set(value) { packageView.text = value }
    var version: CharSequence
        get() = versionView.text
        set(value) { versionView.text = value }
    override fun setOnClickListener(l: OnClickListener?) = clickView.setOnClickListener(l)

    private val iconView: ImageView
    private val nameView: TextView
    private val packageView: TextView
    private val versionView: TextView
    private val clickView: View

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.view_settings_app_info, this, true)

        iconView = root.findViewById(R.id.view_settings_app_info_icon)
        nameView = root.findViewById(R.id.view_settings_app_info_name)
        packageView = root.findViewById(R.id.view_settings_app_info_package)
        versionView = root.findViewById(R.id.view_settings_app_info_version)
        clickView = root.findViewById(R.id.view_settings_app_info_view_info)
    }
}