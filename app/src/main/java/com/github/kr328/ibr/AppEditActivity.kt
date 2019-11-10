package com.github.kr328.ibr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.kr328.ibr.components.AppEditComponent
import com.github.kr328.ibr.view.SettingAppInfo
import com.github.kr328.ibr.view.SettingButton
import com.github.kr328.ibr.view.SettingSwitch
import java.lang.IllegalArgumentException

class AppEditActivity : AppCompatActivity() {
    private lateinit var component: AppEditComponent

    private lateinit var root: View
    private lateinit var online: View

    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var appInfo: SettingAppInfo
    private lateinit var debugSwitch: SettingSwitch
    private lateinit var onlineSwitch: SettingSwitch
    private lateinit var tag: SettingButton
    private lateinit var author: SettingButton
    private lateinit var onlineRule: SettingButton
    private lateinit var localSwitch: SettingSwitch
    private lateinit var localRule: SettingButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_app)

        component = AppEditComponent(MainApplication.fromContext(this),
                intent.data?.host ?: throw IllegalArgumentException())

        root = findViewById(R.id.activity_edit_app_root)
        online = findViewById(R.id.activity_edit_app_online)

        swipe = findViewById(R.id.activity_edit_app_swipe)
        appInfo = findViewById(R.id.activity_edit_app_app_info)
        debugSwitch = findViewById(R.id.activity_edit_app_debug_mode)
        onlineSwitch = findViewById(R.id.activity_edit_app_online_enable)
        tag = findViewById(R.id.activity_edit_app_online_tag)
        author = findViewById<SettingButton>(R.id.activity_edit_app_online_author)
        onlineRule = findViewById<SettingButton>(R.id.activity_edit_app_online_view_rules)
        localSwitch = findViewById<SettingSwitch>(R.id.activity_edit_app_local_enable)
        localRule = findViewById<SettingButton>(R.id.activity_edit_app_local_view_rules)

        swipe.setOnRefreshListener {
            component.commandChannel.sendCommand(AppEditComponent.COMMAND_REFRESH_ONLINE_RULES, null)
        }

        debugSwitch.setOnCheckChangedListener { _, checked ->
            component.commandChannel.sendCommand(AppEditComponent.COMMAND_SET_DEBUG_ENABLED, checked)
        }
        onlineSwitch.setOnCheckChangedListener { _, checked ->
            component.commandChannel.sendCommand(AppEditComponent.COMMAND_SET_ONLINE_ENABLED, checked)
        }
        localSwitch.setOnCheckChangedListener { _, checked ->
            component.commandChannel.sendCommand(AppEditComponent.COMMAND_SET_LOCAL_ENABLED, checked)
        }

        component.onlineRuleSet.observe(this) {
            online.visibility = View.VISIBLE
            tag.summary = it.tag
            author.summary = it.author
        }

        component.onlineRuleCount.observe(this) {
            onlineRule.summary = getString(R.string.edit_app_application_online_rule_set_view_rules_summary, it)
        }

        component.localRuleCount.observe(this) {
            localRule.summary = getString(R.string.edit_app_application_local_rule_set_view_rules_summary, it)
        }

        component.appDate.observe(this) {
            appInfo.packageName = it.packageName
            appInfo.name = it.name
            appInfo.version = it.version
            appInfo.icon = it.icon

            root.visibility = View.VISIBLE
        }

        component.commandChannel.registerReceiver(AppEditComponent.COMMAND_INITIAL_FEATURE_ENABLED) { _, e: AppEditComponent.FeatureEnabled? ->
            debugSwitch.checked = e?.debug ?: false
            onlineSwitch.checked = e?.online ?: false
            localSwitch.checked = e?.local ?: false
        }

        component.commandChannel.registerReceiver(AppEditComponent.COMMAND_SHOW_REFRESHING) { _, e: Boolean? ->
            if (e == null)
                return@registerReceiver

            if (swipe.isRefreshing != e)
                swipe.isRefreshing = e
        }

        onlineRule.setOnClickListener {
            startActivity(Intent(this, RuleViewerActivity::class.java)
                    .setData(Uri.parse("pkg://${component.packageName}"))
                    .putExtra("type", "online"))
        }

        localRule.setOnClickListener {
            startActivity(Intent(this, RuleViewerActivity::class.java)
                    .setData(Uri.parse("pkg://${component.packageName}"))
                    .putExtra("type", "local"))
        }

        if (component.onlineRuleSet.value == null)
            online.visibility = View.GONE
        else
            online.visibility = View.VISIBLE

        root.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()

        component.shutdown()
    }
}