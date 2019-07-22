package com.github.kr328.ibr.fragment

import android.os.Bundle
import com.github.kr328.ibr.R
import com.github.kr328.ibr.model.AppData
import com.github.kr328.ui.fragment.SettingFragment
import com.github.kr328.ui.fragment.holder.AppInfoSettingHolder
import com.github.kr328.ui.fragment.holder.ButtonSettingHolder
import java.util.*

class EditAppFragment(private val appData: AppData) : SettingFragment() {
    override fun getXmlResourceId(): Int = R.xml.settings_edit_app

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val appInfoHolder: AppInfoSettingHolder = getViewHolder("app_info")
        val tagHolder: ButtonSettingHolder = getViewHolder("tag")
        val authorHolder: ButtonSettingHolder = getViewHolder("author")
        val updateHolder: ButtonSettingHolder = getViewHolder("last_update")
        val ruleHolder: ButtonSettingHolder = getViewHolder("rule")

        appInfoHolder.name.text = appData.name
        appInfoHolder.version.text = appData.version
        appInfoHolder.packageName.text = appData.packageName
        appInfoHolder.icon.setImageDrawable(appData.icon)

        tagHolder.summary.text = appData.ruleSet.tag

        authorHolder.summary.text = "Kr328"

        updateHolder.summary.text = Date().toString()

        ruleHolder.title.text = appData.ruleSet.rules[0].tag
        ruleHolder.summary.text = "Hint 22 times"
    }
}