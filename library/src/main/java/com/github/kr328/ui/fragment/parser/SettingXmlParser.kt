package com.github.kr328.ui.fragment.parser

import android.content.Context
import android.content.Intent
import android.content.res.XmlResourceParser
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.kr328.ui.R
import com.github.kr328.ui.fragment.holder.AppInfoSettingHolder
import com.github.kr328.ui.fragment.holder.ButtonSettingHolder
import com.github.kr328.ui.fragment.holder.SettingHolder
import com.github.kr328.ui.fragment.holder.TitleSettingHolder
import org.xmlpull.v1.XmlPullParser
import java.lang.IllegalArgumentException

data class SettingXmlParseResult(val root: View, val holders: Map<String, SettingHolder>) {
    class SettingXmlParseException(message: String?) : Exception(message)
}

fun parseSettingXml(context: Context, inflater: LayoutInflater, parent: ViewGroup?, xml: XmlResourceParser): SettingXmlParseResult {
    if ( xml.eventType != XmlPullParser.START_DOCUMENT && xml.eventType != XmlPullParser.START_TAG && xml.name != "Settings" )
        throw SettingXmlParseResult.SettingXmlParseException("xml root invalid")

    val root = inflater.inflate(R.layout.module_settings_fragment_root, parent, false)
    val container: LinearLayout = root.findViewById(R.id.module_settings_fragment_root)
    val holders = mutableMapOf<String, SettingHolder>()

    while ( xml.eventType != XmlPullParser.END_DOCUMENT ) {
        xml.next()

        if ( xml.eventType != XmlPullParser.START_TAG || xml.name == "Settings" )
            continue

        val child: Pair<View, SettingHolder> = when (xml.name) {
            "app-info" -> parseSettingAppInfoXml(context, inflater, parent, xml)
            "title" -> parseSettingTitleXml(context, inflater, parent, xml)
            "button" -> parseSettingClickableItemXml(context, inflater, parent, xml)
            else -> throw SettingXmlParseResult.SettingXmlParseException("Unsupported settings element ${xml.name}")
        }

        container.addView(child.first)
        child.second.id.takeIf(String::isNotBlank)?.let {
            holders[it] = child.second
        }
    }

    return SettingXmlParseResult(root, holders)
}

private fun parseSettingAppInfoXml(context: Context, inflater: LayoutInflater, parent: ViewGroup?, xml: XmlResourceParser): Pair<View, AppInfoSettingHolder> {
    val id = xml.getAttributeValue(null, "id") ?: ""
    val defaultIcon = xml.getAttributeValue(null, "icon")
    val defaultName = xml.getAttributeValue(null, "name")
    val defaultVersion = xml.getAttributeValue(null, "version")
    val defaultPackage = xml.getAttributeValue(null, "package")
    val infoIcon = xml.getAttributeValue(null, "info_icon")

    val view: View = inflater.inflate(R.layout.module_settings_fragment_app_info, parent, false)
    val holder = AppInfoSettingHolder(
            id = id,
            icon = view.findViewById(R.id.module_settings_fragment_app_info_icon),
            name = view.findViewById(R.id.module_settings_fragment_app_info_name),
            version = view.findViewById(R.id.module_settings_fragment_app_info_version),
            packageName = view.findViewById(R.id.module_settings_fragment_app_info_package)
    )

    defaultIcon?.let { holder.icon.setImageDrawable(context.getDrawable(it.toInt())) }
    defaultName?.let { holder.name.text = it }
    defaultVersion?.let { holder.version.text = it }
    defaultPackage?.let { holder.packageName.text = it }
    infoIcon?.let { resId ->
        with( view.findViewById<View>(R.id.module_settings_fragment_app_info_view_info) ) {
            background = context.parseDrawable(resId)
            setOnClickListener {
                context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:${holder.packageName.text}")))
            }
        }
    }

    return view to holder
}

private fun parseSettingTitleXml(context: Context, inflater: LayoutInflater, parent: ViewGroup?, xml: XmlResourceParser): Pair<View, SettingHolder> {
    val id = xml.getAttributeValue(null, "id") ?: ""
    val title = xml.getAttributeValue(null, "title")
    val color = xml.getAttributeValue(null, "color")

    val view = inflater.inflate(R.layout.module_settings_fragment_title, parent, false)
    val holder = TitleSettingHolder(
            id = id,
            title = view.findViewById(R.id.module_settings_fragment_title_title)
            )

    title?.let { holder.title.text = context.parseString(it) }
    color?.let { holder.title.setTextColor(context.parseColor(it)) }

    return view to holder
}

private fun parseSettingClickableItemXml(context: Context, inflater: LayoutInflater, parent: ViewGroup?, xml: XmlResourceParser): Pair<View, SettingHolder> {
    val id = xml.getAttributeValue(null, "id") ?: ""
    val icon = xml.getAttributeValue(null, "icon")
    val title = xml.getAttributeValue(null, "title")
    val summary = xml.getAttributeValue(null, "summary")

    val view = inflater.inflate(R.layout.module_settings_fragment_clickable_item, parent, false)
    val holder = ButtonSettingHolder(id = id,
            icon = view.findViewById(R.id.module_settings_fragment_clickable_item_icon),
            title = view.findViewById(R.id.module_settings_fragment_clickable_item_title),
            summary = view.findViewById(R.id.module_settings_fragment_clickable_item_summary),
            clickable = view.findViewById(R.id.module_settings_fragment_clickable_item_clickable))

    icon?.let { holder.icon.setImageDrawable(context.parseDrawable(it)) }
    title?.let { holder.title.text = context.parseString(it) }
    summary?.let { holder.summary.text = context.parseString(it) }

    return view to holder
}

private fun Context.parseString(text: String?): String {
    return if ( text != null && text.startsWith("@") )
        this.getString(text.removePrefix("@").toInt())
    else
        text ?: ""
}

private fun Context.parseDrawable(id: String?): Drawable {
    if ( id == null || !id.startsWith("@"))
        throw IllegalArgumentException("Invalid drawable $id")
    return this.getDrawable(id.removePrefix("@").toInt())!!
}

private fun Context.parseColor(text: String?): Int {
    return if ( text != null && text.startsWith("@") )
        this.getColor(text.removePrefix("@").toInt())
    else
        text?.toInt() ?: throw IllegalArgumentException("Invalid color $text")
}
