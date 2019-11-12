package com.github.kr328.ibr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.github.kr328.ibr.components.AppEditComponent
import kotlinx.android.synthetic.main.activity_edit_app.*

class AppEditActivity : AppCompatActivity() {
    private lateinit var component: AppEditComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_app)

        component = AppEditComponent(MainApplication.fromContext(this),
                intent.data?.host ?: throw IllegalArgumentException())

        component.commandChannel.registerReceiver(AppEditComponent.COMMAND_SHOW_EXCEPTION) {_, exception: AppEditComponent.ExceptionType? ->
            showException(exception)
        }

        activity_edit_app_debug_mode.setOnCheckChangedListener { _, checked ->
            component.commandChannel.sendCommand(AppEditComponent.COMMAND_SET_DEBUG_ENABLED, checked)
        }
        activity_edit_app_online_enable.setOnCheckChangedListener { _, checked ->
            component.commandChannel.sendCommand(AppEditComponent.COMMAND_SET_ONLINE_ENABLED, checked)
        }
        activity_edit_app_local_enable.setOnCheckChangedListener { _, checked ->
            component.commandChannel.sendCommand(AppEditComponent.COMMAND_SET_LOCAL_ENABLED, checked)
        }

        component.onlineRuleSet.observe(this) {
            if (it != null) {
                activity_edit_app_online_root.visibility = View.VISIBLE
                activity_edit_app_online_tag.summary = it.tag
                activity_edit_app_online_author.summary = it.author
            }
        }

        component.onlineRuleCount.observe(this) {
            activity_edit_app_online_view_rules.summary = getString(R.string.edit_app_application_online_rule_set_view_rules_summary, it)
        }

        component.localRuleCount.observe(this) {
            activity_edit_app_local_view_rules.summary = getString(R.string.edit_app_application_local_rule_set_view_rules_summary, it)
        }

        component.appDate.observe(this) {
            with(activity_edit_app_app_info) {
                packageName = it.packageName
                name = it.name
                version = it.version
                icon = it.icon
            }

            activity_edit_app_root.visibility = View.VISIBLE
        }

        component.commandChannel.registerReceiver(AppEditComponent.COMMAND_INITIAL_FEATURE_ENABLED) { _, e: AppEditComponent.FeatureEnabled? ->
            activity_edit_app_debug_mode.checked = e?.debug ?: false
            activity_edit_app_online_enable.checked = e?.online ?: false
            activity_edit_app_local_enable.checked = e?.local ?: false
        }

        activity_edit_app_online_view_rules.setOnClickListener {
            startActivity(Intent(this, RuleViewerActivity::class.java)
                    .setData(Uri.parse("pkg://${component.packageName}"))
                    .putExtra("type", "online"))
        }

        activity_edit_app_local_view_rules.setOnClickListener {
            startActivity(Intent(this, RuleViewerActivity::class.java)
                    .setData(Uri.parse("pkg://${component.packageName}"))
                    .putExtra("type", "local"))
        }

        activity_edit_app_online_root.visibility =
                if (component.onlineRuleSet.value == null)
                    View.GONE
                else
                    View.VISIBLE

        activity_edit_app_root.visibility = View.INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_edit_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.activity_edit_remove_rule_set)
            component.commandChannel.sendCommand(AppEditComponent.COMMAND_REMOVE_LOCAL_RULE_SET)

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()

        component.shutdown()
    }

    private fun showException(exception: AppEditComponent.ExceptionType?) {
        when (exception) {
            AppEditComponent.ExceptionType.LOAD_APP_INFO_FAILURE -> {
                Toast.makeText(this,
                        R.string.edit_app_application_exception_load_app_info_failure,
                        Toast.LENGTH_LONG).show()
                finish()
            }
            AppEditComponent.ExceptionType.QUERY_DATA_FROM_SERVICE_FAILURE -> {
                Toast.makeText(this,
                        R.string.edit_app_application_exception_query_data_failure,
                        Toast.LENGTH_LONG).show()
            }
            AppEditComponent.ExceptionType.REFRESH_FAILURE -> {
                Toast.makeText(this,
                        R.string.edit_app_application_exception_refresh_failure,
                        Toast.LENGTH_LONG).show()
            }
            AppEditComponent.ExceptionType.PUSH_DATA_TO_SERVICE_FAILURE -> {
                Toast.makeText(this,
                        R.string.edit_app_application_exception_push_data_to_service_failure,
                        Toast.LENGTH_LONG).show()
            }
        }
    }
}