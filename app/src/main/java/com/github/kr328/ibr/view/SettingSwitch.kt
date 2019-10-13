package com.github.kr328.ibr.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.github.kr328.ibr.R

class SettingSwitch @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defaultStyleAttr: Int = 0, defaultStyleRes: Int = 0) :
        FrameLayout(context, attributeSet, defaultStyleAttr, defaultStyleRes) {
    var title: CharSequence
        get() = titleView.text
        set(value) {
            titleView.text = value
        }
    var summary: CharSequence
        get() = summaryView.text
        set(value) {
            summaryView.text = value
            if (value.isEmpty())
                summaryView.visibility = View.GONE
        }
    var icon: Drawable?
        get() {
            throw IllegalArgumentException("Unsupported")
        }
        set(value) {
            iconView.setImageDrawable(value)
        }
    var checked: Boolean
        get() {
            return switch.isChecked
        }
        set(value) {
            switch.isChecked = value
        }

    override fun setOnClickListener(l: OnClickListener?) = clickable.setOnClickListener(l)
    fun setOnCheckChangedListener(l: CompoundButton.OnCheckedChangeListener) = switch.setOnCheckedChangeListener(l)

    private val titleView: TextView
    private val summaryView: TextView
    private val iconView: ImageView
    private val switch: SwitchCompat
    private val clickable: View
    private val root: View = LayoutInflater.from(context).inflate(R.layout.view_settings_switch, this, true)

    init {
        clickable = root.findViewById(R.id.view_settings_switch_clickable)
        titleView = root.findViewById(R.id.view_settings_switch_title)
        summaryView = root.findViewById(R.id.view_settings_switch_summary)
        iconView = root.findViewById(R.id.view_settings_switch_icon)
        switch = root.findViewById(R.id.view_settings_switch_switch)

        context.theme.obtainStyledAttributes(attributeSet, R.styleable.custom, defaultStyleAttr, defaultStyleRes).apply {
            title = getString(R.styleable.custom_title) ?: ""
            summary = getString(R.styleable.custom_summary) ?: ""
            icon = getDrawable(R.styleable.custom_icon)
        }

        clickable.setOnClickListener {
            switch.performClick()
        }
    }
}