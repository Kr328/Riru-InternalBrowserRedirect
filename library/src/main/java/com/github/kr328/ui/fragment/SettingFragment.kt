package com.github.kr328.ui.fragment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Adapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.kr328.ui.R
import kotlin.concurrent.thread

abstract class SettingFragment : Fragment() {
    protected abstract class Setting
    protected data class AppInfo(val icon: Drawable, val name: String, val version: String, val packageName: String, val infoIcon: Drawable? = null): Setting()
    protected data class Title(val title: String) : Setting()
    protected data class Button(val title: String, val data: String, val icon: Drawable? = null, val clickListener: View.OnClickListener? = null) : Setting()

    abstract val colorAccent: Int

    private val layout: LinearLayout by lazy {
        view?.findViewById<LinearLayout>(R.id.module_settings_fragment_root_root) ?: throw IllegalArgumentException("Invalid layout")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.module_settings_fragment_root, container, false)
    }

    protected fun updateSettings(settings: List<Setting>) {
        thread {
            val views = settings.map(this::createSettingView)

            requireActivity().runOnUiThread {
                layout.removeAllViews()
                views.forEach(layout::addView)
                layout.startAnimation(AlphaAnimation(0.5f, 1.0f).apply {
                    duration = 500
                })
            }
        }
    }

    private fun createSettingView(setting: Setting): View {
        return when ( setting ) {
            is AppInfo -> layoutInflater.inflate(R.layout.module_settings_fragment_app_info, layout, false).apply {
                tag = setting
                findViewById<ImageView>(R.id.module_settings_fragment_app_info_icon).setImageDrawable(setting.icon)
                findViewById<TextView>(R.id.module_settings_fragment_app_info_name).text = setting.name
                findViewById<TextView>(R.id.module_settings_fragment_app_info_version).text = setting.version
                findViewById<TextView>(R.id.module_settings_fragment_app_info_package).text = setting.packageName
                findViewById<View>(R.id.module_settings_fragment_app_info_view_info).apply {
                    background = setting.infoIcon
                    setOnClickListener {
                        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:${setting.packageName}")))
                    }
                }
            }
            is Title -> layoutInflater.inflate(R.layout.module_settings_fragment_title, layout, false).apply {
                tag = setting
                findViewById<TextView>(R.id.module_settings_fragment_title_title).apply {
                    text = setting.title
                    setTextColor(colorAccent)
                }
            }
            is Button -> layoutInflater.inflate(R.layout.module_settings_fragment_button, layout, false).apply {
                tag = setting
                findViewById<ImageView>(R.id.module_settings_fragment_button_icon).setImageDrawable(setting.icon)
                findViewById<TextView>(R.id.module_settings_fragment_button_title).text = setting.title
                findViewById<TextView>(R.id.module_settings_fragment_button_data).text = setting.data
                findViewById<View>(R.id.module_settings_fragment_clickable_item_clickable).setOnClickListener(setting.clickListener)
            }
            else -> throw IllegalArgumentException("Unknown setting $setting")
        }
    }
}