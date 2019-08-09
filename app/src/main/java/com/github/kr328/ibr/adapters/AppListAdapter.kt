package com.github.kr328.ibr.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.ibr.R
import com.github.kr328.ibr.model.AppListData

class AppListAdapter(private val context: Context, private val onClickListener: (pkg: String) -> Unit) :
        RecyclerView.Adapter<AppListAdapter.AppListViewHolder>() {
    class AppListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.adapter_app_list_name)
        val icon: ImageView = view.findViewById(R.id.adapter_app_list_icon)
        val description: TextView = view.findViewById(R.id.adapter_app_list_description)
    }

    var appListData: AppListData = AppListData(emptyList())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListViewHolder {
        return AppListViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_app_list, parent, false).also {
            it.setOnClickListener { view ->
                onClickListener.invoke(view.tag as String? ?: "")
            }
        })
    }

    override fun getItemCount(): Int {
        return appListData.elements.size
    }

    override fun onBindViewHolder(holder: AppListViewHolder, position: Int) {
        val data = appListData.elements[position]

        holder.itemView.tag = data.packageName

        holder.name.text = data.name
        holder.icon.setImageDrawable(data.icon)
        holder.description.text = data.appState.toI18nString()

        if (data.appState.enabled)
            holder.description.setTextColor(context.getColor(R.color.colorAccent))
        else
            holder.description.setTextColor(Color.GRAY)
    }

    private fun AppListData.AppState.toI18nString(): String {
        val sb = StringBuilder()

        sb.append(context.getString(
                if (enabled) R.string.app_list_application_state_enabled
                else R.string.app_list_application_state_disabled))
        sb.append(" (")
        sb.append(context.getString(
                when (ruleType) {
                    AppListData.RuleType.ONLINE -> R.string.app_list_application_state_online_rule
                    AppListData.RuleType.PRELOAD -> R.string.app_list_application_state_preload_rule
                    AppListData.RuleType.UNKNOWN -> R.string.app_list_application_state_unknown_rule
                }
        ))
        sb.append(")")

        return sb.toString()
    }
}