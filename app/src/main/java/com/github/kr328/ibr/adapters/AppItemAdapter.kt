package com.github.kr328.ibr.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.github.kr328.ibr.R
import kotlinx.android.synthetic.main.adapter_app_item.view.*

class AppItemAdapter(private val context: Context,
                     private val appInfoData: List<Item>) : BaseAdapter() {
    data class Item(val name: String, val packageName: String)

    override fun getItem(position: Int): Any {
        return appInfoData[position]
    }

    override fun getItemId(position: Int): Long {
        return appInfoData[position].packageName.hashCode().toLong()
    }

    override fun getCount(): Int {
        return appInfoData.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return (convertView ?: LayoutInflater.from(context).inflate(R.layout.adapter_app_item, parent, false)).apply {
            val current = appInfoData[position]

            adapter_app_item_icon.setImageDrawable(context.packageManager.getApplicationIcon(current.packageName))
            adapter_app_item_name.text = current.name
            adapter_app_item_description.text = current.packageName
        }
    }
}