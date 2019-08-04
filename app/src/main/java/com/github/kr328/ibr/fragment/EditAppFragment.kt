package com.github.kr328.ibr.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import com.github.kr328.ibr.R
import com.github.kr328.ibr.model.AppData
import com.github.kr328.ui.fragment.SettingFragment
import java.util.*

class EditAppFragment(private val appData: AppData) : SettingFragment() {
    private val handler = Handler()

    override val colorAccent: Int by lazy {
        requireContext().getColor(R.color.colorAccent)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        updateSettings(listOf(
                AppInfo(appData.icon, appData.name, appData.version, appData.packageName, drawable(R.drawable.ic_info)),
                Title(string(R.string.edit_app_application_rule_set)),
                Button(string(R.string.edit_app_application_rule_set_tag), appData.ruleSet.tag, drawable(R.drawable.ic_label)),
                Button(string(R.string.edit_app_application_rule_set_author), "Kr328", drawable(R.drawable.ic_person)),
                Button(string(R.string.edit_app_application_rule_set_last_update), Date().toString(), drawable(R.drawable.ic_update)),
                Title(string(R.string.edit_app_application_rule))
        ) + appData.ruleSet.rules.map { Button(it.tag, it.urlPath.toString()) })
    }

    private fun drawable(resId: Int): Drawable? = requireContext().getDrawable(resId)
    private fun string(resId: Int): String = requireContext().getString(resId)
}