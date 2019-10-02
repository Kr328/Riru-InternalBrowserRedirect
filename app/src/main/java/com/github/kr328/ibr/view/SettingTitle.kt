package com.github.kr328.ibr.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.github.kr328.ibr.R

class SettingTitle @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defaultStyleAttr: Int = 0, defaultStyleRes: Int = 0) :
        FrameLayout(context, attributeSet, defaultStyleAttr, defaultStyleRes) {
    var title: CharSequence
        get() = titleView.text
        set(value) { titleView.text = value }

    private val titleView: TextView;

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.view_settings_title, this, true)

        titleView = root.findViewById(R.id.view_settings_title_title)

        context.theme.obtainStyledAttributes(attributeSet, R.styleable.custom, defaultStyleAttr, defaultStyleRes).apply {
            title = getString(R.styleable.custom_title) ?: ""
        }
    }
}