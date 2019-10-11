package com.github.kr328.ibr.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.github.kr328.ibr.R
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingHeaderSwitch @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
        FrameLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    var title: CharSequence
        get() = switch.text
        set(value) {
            switch.text = value
        }

    fun setOnCheckedChangeListener(listener: (view: View, checked: Boolean) -> Unit) {
        switch.setOnCheckedChangeListener { view, checked ->
            listener(view, checked)
        }
    }

    private val switch: SwitchMaterial

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.view_settings_header_switch, this, true)

        switch = root.findViewById(R.id.view_settings_header_switch)

        context.theme.obtainStyledAttributes(attributeSet, R.styleable.custom, defStyleAttr, defStyleRes).apply {
            switch.text = getString(R.styleable.custom_title) ?: ""
        }
    }
}
