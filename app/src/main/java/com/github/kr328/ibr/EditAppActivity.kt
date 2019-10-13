package com.github.kr328.ibr

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.kr328.ibr.action.EditAppCreatedActivityAction
import com.github.kr328.ibr.action.EditAppRefreshAction
import com.github.kr328.ibr.action.EditAppStartedActivityAction
import com.github.kr328.ibr.state.EditAppState
import com.github.kr328.ibr.view.SettingAppInfo
import com.github.kr328.ibr.view.SettingButton
import com.github.kr328.ibr.view.SettingSwitch
import org.rekotlin.StoreSubscriber

class EditAppActivity : AppCompatActivity(), StoreSubscriber<EditAppState?> {
    private val currentPackageName by lazy { intent.data?.host ?: { finish(); "" }() }

    private val store by lazy { MainApplication.fromContext(this).store }

    private val root by lazy { findViewById<View>(R.id.activity_edit_app_root) }
    private val online by lazy { findViewById<View>(R.id.activity_edit_app_online) }

    private val swipe by lazy { findViewById<SwipeRefreshLayout>(R.id.activity_edit_app_swipe) }
    private val appInfo by lazy { findViewById<SettingAppInfo>(R.id.activity_edit_app_app_info) }
    private val onlineSwitch by lazy { findViewById<SettingSwitch>(R.id.activity_edit_app_online_enable) }
    private val tag by lazy { findViewById<SettingButton>(R.id.activity_edit_app_online_tag) }
    private val author by lazy { findViewById<SettingButton>(R.id.activity_edit_app_online_author) }
    private val onlineRule by lazy { findViewById<SettingButton>(R.id.activity_edit_app_online_view_rules)}
    private val localSwitch by lazy { findViewById<SettingSwitch>(R.id.activity_edit_app_local_enable)}
    private val localRule by lazy { findViewById<SettingButton>(R.id.activity_edit_app_local_view_rules) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_app)

        root.visibility = View.INVISIBLE
        swipe.isRefreshing = true
        swipe.setOnRefreshListener {
            store.dispatch(EditAppRefreshAction(currentPackageName))
        }

        store.dispatch(EditAppCreatedActivityAction(currentPackageName))
    }

    override fun onStart() {
        super.onStart()

        store.subscribe(this) { subscription ->
            subscription.select {
                it.editAppState
            }
        }

        store.dispatch(EditAppStartedActivityAction(currentPackageName))
    }

    override fun onStop() {
        super.onStop()

        store.unsubscribe(this)
    }

    override fun newState(state: EditAppState?) {
        if (state == null)
            return

        runOnUiThread {
            root.visibility = View.VISIBLE

            if (swipe.isRefreshing != state.isRefreshing)
                swipe.isRefreshing = state.isRefreshing

            appInfo.name = state.name
            appInfo.version = state.version
            appInfo.packageName = state.packageName
            appInfo.icon = state.icon

            if (state.onlineRules != null) {
                onlineSwitch.checked = state.onlineEnable
                online.visibility = View.VISIBLE
                onlineSwitch.checked = state.onlineEnable
                tag.summary = state.onlineRules.tag
                author.summary = state.onlineRules.authors
                onlineRule.summary = getString(R.string.edit_app_application_online_rule_set_view_rules_summary,
                        state.onlineRules.rules.size)
            } else
                online.visibility = View.GONE

            if ( state.localRules != null) {
                localSwitch.checked = state.localEnable
                localRule.summary = getString(R.string.edit_app_application_local_rule_set_view_rules_summary,
                        state.localRules.rules.size)
            }
            else {
                localRule.summary = getString(R.string.edit_app_application_local_rule_set_view_rules_summary, 0)
            }
        }
    }
}