package com.github.kr328.ibr.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.github.kr328.ibr.R
import com.github.kr328.ibr.model.AppListData

class AppListAdapter(private val context: Context, private val appListData: AppListData) : BaseAdapter() {
    override fun getView(index: Int, cache: View?, parent: ViewGroup?): View {
        return (cache ?: LayoutInflater.from(context).inflate(R.layout.adapter_app_list, parent, false)).also { view ->
            with (appListData.elements[index]) {
                view.findViewById<TextView>(R.id.adapter_app_list_name).text = this.name
                view.findViewById<TextView>(R.id.adapter_app_list_description).text = this.appState.toI18nString()
                view.findViewById<ImageView>(R.id.adapter_app_list_icon).setImageDrawable(this.icon)
            }
        }
    }

    override fun getItem(index: Int): Any = appListData.elements[index]

    override fun getItemId(index: Int): Long = appListData.elements[index].hashCode().toLong()

    override fun getCount(): Int = appListData.elements.size

    private fun AppListData.AppState.toI18nString(): String {
        val sb = StringBuilder()

        sb.append(context.getString(
                if (enabled) R.string.app_list_application_state_enabled
                else R.string.app_list_application_state_disabled))
        sb.append(" (")
        sb.append(context.getString(
                when (ruleType) {
                    AppListData.AppState.RULE_TYPE_LOCAL -> R.string.app_list_application_state_local_rule
                    AppListData.AppState.RULE_TYPE_ONLINE -> R.string.app_list_application_state_online_rule
                    AppListData.AppState.RULE_TYPE_UNKNOWN -> R.string.app_list_application_state_local_unknown
                    else -> R.string.app_list_application_state_local_unknown
                }
        ))
        sb.append(")")

        return sb.toString()
    }
}