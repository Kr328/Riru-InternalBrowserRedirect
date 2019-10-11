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
import com.github.kr328.ibr.model.AppListElement

class AppListAdapter(private val context: Context, private val onClickListener: (pkg: String) -> Unit) :
        RecyclerView.Adapter<AppListAdapter.AppListViewHolder>() {
    class AppListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.adapter_app_list_name)
        val icon: ImageView = view.findViewById(R.id.adapter_app_list_icon)
        val description: TextView = view.findViewById(R.id.adapter_app_list_description)
    }

    var appListElement: List<AppListElement> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListViewHolder {
        return AppListViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_app_list, parent, false).also {
            it.setOnClickListener { view ->
                onClickListener.invoke(view.tag as String? ?: "")
            }
        })
    }

    override fun getItemCount(): Int {
        return appListElement.size
    }

    override fun onBindViewHolder(holder: AppListViewHolder, position: Int) {
        val data = appListElement[position]

        holder.itemView.tag = data.packageName

        holder.name.text = data.name
        holder.icon.setImageDrawable(data.icon)
        holder.description.text = data.ruleStatus.toI18nString()

        if (data.ruleStatus.contains(AppListElement.RuleStatus.ENABLED))
            holder.description.setTextColor(context.getColor(R.color.colorAccent))
        else
            holder.description.setTextColor(Color.GRAY)
    }

    private fun Set<AppListElement.RuleStatus>.toI18nString(): String {
        val sb = StringBuilder()

        if (contains(AppListElement.RuleStatus.ENABLED))
            sb.append(context.getString(R.string.app_list_application_state_enabled))
        else
            sb.append(context.getString(R.string.app_list_application_state_disabled))
        sb.append(" (")

        when {
            contains(AppListElement.RuleStatus.ONLINE) and contains(AppListElement.RuleStatus.LOCAL) -> {
                sb.append(context.getString(R.string.app_list_application_state_online_rule))
                sb.append(", ")
                sb.append(context.getString(R.string.app_list_application_state_local_rule))
            }
            contains(AppListElement.RuleStatus.ONLINE) ->
                sb.append(context.getString(R.string.app_list_application_state_online_rule))
            contains(AppListElement.RuleStatus.LOCAL) ->
                sb.append(context.getString(R.string.app_list_application_state_local_rule))
            else ->
                sb.append(context.getString(R.string.app_list_application_state_preload_rule))
        }

        sb.append(")")

        return sb.toString()
    }
}