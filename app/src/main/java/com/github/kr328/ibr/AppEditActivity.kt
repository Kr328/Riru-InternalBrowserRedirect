package com.github.kr328.ibr

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.kr328.ibr.action.*
import com.github.kr328.ibr.components.AppEditComponent
import com.github.kr328.ibr.state.EditAppState
import com.github.kr328.ibr.view.SettingAppInfo
import com.github.kr328.ibr.view.SettingButton
import com.github.kr328.ibr.view.SettingSwitch
import org.rekotlin.StoreSubscriber

class AppEditActivity : AppCompatActivity() {
    private val component by lazy { AppEditComponent(MainApplication.fromContext(this),
            intent.data?.host ?: { finish(); "" }()) }

    private val root by lazy { findViewById<View>(R.id.activity_edit_app_root) }
    private val online by lazy { findViewById<View>(R.id.activity_edit_app_online) }

    private val swipe by lazy { findViewById<SwipeRefreshLayout>(R.id.activity_edit_app_swipe) }
    private val appInfo by lazy { findViewById<SettingAppInfo>(R.id.activity_edit_app_app_info) }
    private val onlineSwitch by lazy { findViewById<SettingSwitch>(R.id.activity_edit_app_online_enable) }
    private val tag by lazy { findViewById<SettingButton>(R.id.activity_edit_app_online_tag) }
    private val author by lazy { findViewById<SettingButton>(R.id.activity_edit_app_online_author) }
    private val onlineRule by lazy { findViewById<SettingButton>(R.id.activity_edit_app_online_view_rules) }
    private val localSwitch by lazy { findViewById<SettingSwitch>(R.id.activity_edit_app_local_enable) }
    private val localRule by lazy { findViewById<SettingButton>(R.id.activity_edit_app_local_view_rules) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_app)

        swipe.setOnRefreshListener {

        }
        onlineSwitch.setOnCheckChangedListener { _, _ ->

        }
        localSwitch.setOnCheckChangedListener { _, _ ->

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

        if ( component.onlineRuleSet.value == null )
            online.visibility = View.GONE
        else
            online.visibility = View.VISIBLE

        root.visibility = View.INVISIBLE
    }
}