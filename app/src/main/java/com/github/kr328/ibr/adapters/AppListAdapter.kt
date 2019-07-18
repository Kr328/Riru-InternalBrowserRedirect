package com.github.kr328.ibr.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.github.kr328.ibr.R

class AppListAdapter(private val context: Context, private val packages: List<AppListElement>) : BaseAdapter() {
    data class AppListElement(val name: String, val description: String, val icon: Drawable)

    override fun getView(index: Int, cache: View?, parent: ViewGroup?): View {
        return (cache ?: LayoutInflater.from(context).inflate(R.layout.adapter_app_list, parent, false)).also { view ->
            with (packages[index]) {
                view.findViewById<TextView>(R.id.adapter_app_list_name).text = this.name
                view.findViewById<TextView>(R.id.adapter_app_list_description).text = this.description
                view.findViewById<ImageView>(R.id.adapter_app_list_icon).setImageDrawable(this.icon)
            }
        }
    }

    override fun getItem(index: Int): Any = packages[index]

    override fun getItemId(index: Int): Long = packages[index].hashCode().toLong()

    override fun getCount(): Int = packages.size
}