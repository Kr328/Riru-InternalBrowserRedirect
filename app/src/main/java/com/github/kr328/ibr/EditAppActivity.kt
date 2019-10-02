package com.github.kr328.ibr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.ibr.adapters.SettingsAdapter
import com.github.kr328.ibr.controller.EditAppController
import com.github.kr328.ibr.model.AppData
import com.github.kr328.ibr.model.Settings
import com.google.android.material.snackbar.Snackbar

class EditAppActivity : AppCompatActivity(), EditAppController.Callback {
    private lateinit var root: FrameLayout
    private lateinit var controller: EditAppController
    private lateinit var main: RecyclerView
    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pkg = intent.data?.authority

        if (pkg == null) {
            finish()
            return
        }

        setContentView(R.layout.activity_edit_app)

        root = findViewById(R.id.activity_edit_app_root)
        main = findViewById(R.id.activity_edit_app_main)
        controller = EditAppController(this, pkg, this)

        main.layoutManager = LinearLayoutManager(this)
        main.adapter = SettingsAdapter(this)
    }

    override fun onStart() {
        super.onStart()

        controller.onStart()
    }

    override fun onStop() {
        super.onStop()

        controller.onStop()
    }

    override fun updateAppData(enabled: Boolean, appData: AppData) {
        val switchEnable = appData.onlineRuleSet?.rules?.size ?: -1 > 0

        val oldSettings = (main.adapter as SettingsAdapter).settings
        val newSettings = listOf(
                Settings.HeaderSwitch(switchEnable, enabled, getString(R.string.edit_app_application_enable), controller::setPackageEnabled),
                Settings.AppInfo(appData.icon, appData.name, appData.version, appData.packageName) {
                    startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:${appData.packageName}")))
                }
        ) + (appData.onlineRuleSet?.let { ruleSet -> listOf(
                Settings.Title(getString(R.string.edit_app_application_rule_set)),
                Settings.Button(getDrawable(R.drawable.ic_label), getString(R.string.edit_app_application_rule_set_tag), ruleSet.tag),
                Settings.Button(getDrawable(R.drawable.ic_person), getString(R.string.edit_app_application_rule_set_author), ruleSet.authors.emptyUnknown()),
                Settings.Title(getString(R.string.edit_app_application_rule))
            ) + ruleSet.rules.map { rule ->
                Settings.Button(null, rule.tag, rule.urlSource.toString())
            }
        } ?: emptyList())

        val result = DiffUtil.calculateDiff(object: DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldSettings[oldItemPosition]::class == newSettings[newItemPosition]::class
            }

            override fun getOldListSize(): Int {
                return oldSettings.size
            }

            override fun getNewListSize(): Int {
                return newSettings.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldSettings[oldItemPosition] == newSettings[newItemPosition]
            }
        })

        runOnUiThread {
            with ( main.adapter as SettingsAdapter) {
                result.dispatchUpdatesTo(this)
                this.settings = newSettings
            }
        }
    }

    override fun showUpdating() {
        runOnUiThread {
            if ( snackBar == null )
                snackBar = Snackbar.make(root, R.string.edit_app_application_updating, Snackbar.LENGTH_INDEFINITE)
            snackBar?.show()
        }
    }

    override fun closeUpdating() {
        runOnUiThread {
            if ( snackBar != null )
                snackBar?.dismiss()
            snackBar = null
        }
    }

    override fun onError(error: EditAppController.ErrorType) {
        when ( error ) {
            EditAppController.ErrorType.UPDATE_PACKAGES_FAILURE -> Snackbar.make(root, R.string.edit_app_application_update_failure, Snackbar.LENGTH_LONG).show()
        }
    }

    fun String.emptyUnknown(): String =
        takeIf(String::isNotEmpty) ?: getString(R.string.edit_app_application_rule_set_unknown)
}