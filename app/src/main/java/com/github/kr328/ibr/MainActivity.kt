package com.github.kr328.ibr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.ibr.adapters.AppListAdapter
import com.github.kr328.ibr.controller.AppListController
import com.github.kr328.ibr.data.sources.ServiceSource
import com.github.kr328.ibr.model.AppListData
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), AppListController.Callback {
    private lateinit var controller: AppListController
    private lateinit var root: LinearLayout
    private lateinit var appList: RecyclerView
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        root = findViewById(R.id.activity_main_root)
        appList = findViewById(R.id.activity_main_main_list)
        progress = findViewById(R.id.activity_main_progress)

        appList.adapter = AppListAdapter(this) {
            startActivity(Intent(this, EditAppActivity::class.java).setData(Uri.parse("package://$it")))
        }
        appList.layoutManager = LinearLayoutManager(this)

        controller = AppListController(this, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.activity_main_menu_refresh ->
                controller.forceRefresh()
            R.id.activity_main_menu_settings ->
                startActivity(Intent(this, SettingsActivity::class.java))
            R.id.activity_main_menu_about ->
                CustomTabsIntent.Builder().build().launchUrl(this, Uri.parse(Constants.HELP_URL))
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    override fun onStart() {
        super.onStart()

        controller.onStart()
    }

    override fun onStop() {
        super.onStop()

        controller.onStop()
    }

    override fun showProgress() {
        runOnUiThread {
            progress.visibility = View.VISIBLE
        }
    }

    override fun closeProgress() {
        runOnUiThread {
            progress.visibility = View.INVISIBLE
        }
    }

    override fun updateAppList(data: AppListData) {
        val adapter = appList.adapter as AppListAdapter
        val oldData = adapter.appListData

        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    oldData.elements[oldItemPosition].packageName == data.elements[newItemPosition].packageName

            override fun getOldListSize(): Int = oldData.elements.size

            override fun getNewListSize(): Int = data.elements.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    oldData.elements[oldItemPosition].equalsBase(data.elements[newItemPosition])
        })

        runOnUiThread {
            result.dispatchUpdatesTo(adapter)
            adapter.appListData = data
        }
    }

    override fun onError(error: AppListController.ErrorType, extras: Any) {
        when (error) {
            AppListController.ErrorType.INVALID_SERVICE -> {
                val resId = when ( extras as ServiceSource.RCStatus ) {
                    ServiceSource.RCStatus.RUNNING -> R.string.app_list_application_error_invalid_service_message_unknown
                    ServiceSource.RCStatus.RIRU_NOT_LOADED -> R.string.app_list_application_error_invalid_service_message_riru_not_load
                    ServiceSource.RCStatus.RIRU_NOT_CALL_SYSTEM_SERVER_FORKED -> R.string.app_list_application_error_invalid_service_message_not_call_fork
                    ServiceSource.RCStatus.INJECT_FAILURE -> R.string.app_list_application_error_invalid_service_message_inject_failure
                    ServiceSource.RCStatus.SERVICE_NOT_CREATED -> R.string.app_list_application_error_invalid_service_message_service_not_created
                    ServiceSource.RCStatus.UNABLE_TO_HANDLE_REQUEST -> R.string.app_list_application_error_invalid_service_message_service_unable_to_handle
                    ServiceSource.RCStatus.SYSTEM_BLOCK_IPC -> R.string.app_list_application_error_invalid_service_message_system_block_ipc
                    ServiceSource.RCStatus.SERVICE_VERSION_NOT_MATCHES -> R.string.app_list_application_error_invalid_service_message_service_version_not_matches
                    ServiceSource.RCStatus.UNKNOWN -> R.string.app_list_application_error_invalid_service_message_unknown
                }

                AlertDialog.Builder(this)
                        .setTitle(R.string.app_list_application_error_invalid_service_title)
                        .setMessage(getString(resId)
                                .split("\n").joinToString("\n", transform = String::trim))
                        .setCancelable(false)
                        .setPositiveButton(R.string.app_list_application_error_invalid_service_button_ok) { _, _ -> finish() }
                        .show()
            }
            AppListController.ErrorType.UPDATE_FAILURE -> Snackbar.make(root, R.string.app_list_application_error_update_failure, Snackbar.LENGTH_LONG).show()
        }
    }
}

