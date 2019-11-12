package com.github.kr328.ibr.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.ibr.R
import com.github.kr328.ibr.components.RuleViewerComponent

class RuleViewerAdapter(private val context: Context) : RecyclerView.Adapter<RuleViewerAdapter.Holder>() {
    var rules: List<RuleViewerComponent.RuleData> = emptyList()
    var listener: (RuleViewerComponent.RuleData) -> Unit = {}

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val clickable: View = view.findViewById(R.id.adapter_rule_item_clickable)
        val title: TextView = view.findViewById(R.id.adapter_rule_item_title)
        val summary: TextView = view.findViewById(R.id.adapter_rule_item_summary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(context).inflate(R.layout.adapter_rule_item, parent, false))
    }

    override fun getItemCount(): Int {
        return rules.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val current = rules[position]

        holder.title.text = current.tag
        holder.summary.text = current.urlSource
        holder.clickable.setOnClickListener {
            listener(current)
        }
    }
}