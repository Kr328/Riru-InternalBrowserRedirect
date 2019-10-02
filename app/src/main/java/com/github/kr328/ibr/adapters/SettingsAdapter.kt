package com.github.kr328.ibr.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.ibr.R
import com.github.kr328.ibr.model.Settings
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var settings: List<Settings> = emptyList()

    class TitleHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.view_settings_title_title)

        companion object {
            const val TYPE = 1
        }
    }

    class AppInfoHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.view_settings_app_info_icon)
        val name: TextView = view.findViewById(R.id.view_settings_app_info_name)
        val version: TextView = view.findViewById(R.id.view_settings_app_info_version)
        val packageName: TextView = view.findViewById(R.id.view_settings_app_info_package)
        val info: View = view.findViewById(R.id.view_settings_app_info_view_info)

        companion object {
            const val TYPE = 2
        }
    }

    class ButtonHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.view_settings_button_icon)
        val title: TextView = view.findViewById(R.id.view_settings_button_title)
        val summary: TextView = view.findViewById(R.id.view_settings_button_summary)
        val clickable: View = view.findViewById(R.id.adapter_settings_button_clickable)

        companion object {
            const val TYPE = 3
        }
    }

    class HeaderSwitchHolder(view: View) : RecyclerView.ViewHolder(view) {
        val switch: SwitchMaterial = view.findViewById(R.id.view_settings_header_switch)

        companion object {
            const val TYPE = 4
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)

        return when ( viewType ) {
            TitleHolder.TYPE -> TitleHolder(layoutInflater.inflate(R.layout.view_settings_title, parent, false))
            AppInfoHolder.TYPE -> AppInfoHolder(layoutInflater.inflate(R.layout.view_settings_app_info, parent, false))
            ButtonHolder.TYPE -> ButtonHolder(layoutInflater.inflate(R.layout.view_settings_button, parent, false))
            HeaderSwitchHolder.TYPE -> HeaderSwitchHolder(layoutInflater.inflate(R.layout.view_settings_header_switch, parent, false))
            else -> object: RecyclerView.ViewHolder(View(context)) {}
        }
    }

    override fun getItemCount(): Int = settings.size

    override fun getItemViewType(position: Int): Int {
        return when (settings[position]) {
            is Settings.Title -> TitleHolder.TYPE
            is Settings.AppInfo -> AppInfoHolder.TYPE
            is Settings.HeaderSwitch -> HeaderSwitchHolder.TYPE
            is Settings.Button -> ButtonHolder.TYPE
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val data = settings[position]) {
            is Settings.Title -> with ( holder as TitleHolder ) {
                title.text = data.title
            }
            is Settings.AppInfo -> with ( holder as AppInfoHolder ) {
                icon.setImageDrawable(data.icon)
                name.text = data.name
                packageName.text = data.packageName
                version.text = data.version
                info.setOnClickListener {
                    data.onInfoClicked.invoke()
                }
            }
            is Settings.HeaderSwitch -> with ( holder as HeaderSwitchHolder ) {
                if ( !data.enabled ) {
                    this.switch.isEnabled = false
                    this.switch.alpha = 0.5f
                }

                switch.setOnCheckedChangeListener(null)
                switch.isChecked = data.state
                switch.text = data.title
                switch.setOnCheckedChangeListener { _, checked ->
                    data.onCheckedChangeListener.invoke(checked)
                }
            }
            is Settings.Button -> with ( holder as ButtonHolder ) {
                icon.setImageDrawable(data.icon)
                title.text = data.title
                summary.text = data.summary
                clickable.setOnClickListener {
                    data.onClicked.invoke()
                }
            }
        }
    }
}