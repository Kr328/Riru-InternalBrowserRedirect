package com.github.kr328.ibr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.kr328.ibr.adapters.AppListAdapter
import com.github.kr328.ibr.components.AppListComponent
import com.github.kr328.ibr.model.AppListElement

class MainActivity : AppCompatActivity() {
    private val swipe: SwipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.activity_main_main_swipe) }
    private val appList: RecyclerView by lazy { findViewById<RecyclerView>(R.id.activity_main_main_list) }
    private val component: AppListComponent by lazy { AppListComponent(MainApplication.fromContext(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appList.adapter = AppListAdapter(this) {
            startActivity(Intent(this, AppEditActivity::class.java).setData(Uri.parse("package://$it")))
        }
        appList.layoutManager = LinearLayoutManager(this)

        swipe.setOnRefreshListener {
            component.commandChannel.sendCommand(AppListComponent.COMMAND_REFRESH_ONLINE_RULES, true)
        }

        component.elements.observe(this, this::updateList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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

        component.commandChannel.registerReceiver(AppListComponent.COMMAND_SHOW_REFRESHING) { _, show: Boolean? ->
            runOnUiThread {
                if (show != swipe.isRefreshing) {
                    swipe.isRefreshing = show ?: false
                }
            }
        }

        component.commandChannel.sendCommand(AppListComponent.COMMAND_REFRESH_ONLINE_RULES, false)
    }

    override fun onStop() {
        super.onStop()

        component.commandChannel.unregisterReceiver(AppListComponent.COMMAND_SHOW_REFRESHING)
    }

    override fun onDestroy() {
        super.onDestroy()

        component.shutdown()
    }

    private fun updateList(list: List<AppListElement>) {
        val adapter = appList.adapter as AppListAdapter
        val oldData = adapter.appListElement

        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    oldData[oldItemPosition].packageName == list[newItemPosition].packageName

            override fun getOldListSize(): Int = list.size

            override fun getNewListSize(): Int = oldData.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    oldData[oldItemPosition].equalsBase(list[newItemPosition])
        })

        adapter.appListElement = list
        result.dispatchUpdatesTo(adapter)
    }

    //    private fun onError(error: AppListController.ErrorType, extras: Any) {
//        when (error) {
//            AppListController.ErrorType.INVALID_SERVICE -> {
//                val resId = when ( extras as RemoteService.RCStatus ) {
//                    RemoteService.RCStatus.RUNNING -> R.string.app_list_application_error_invalid_service_message_unknown
//                    RemoteService.RCStatus.RIRU_NOT_LOADED -> R.string.app_list_application_error_invalid_service_message_riru_not_load
//                    RemoteService.RCStatus.RIRU_NOT_CALL_SYSTEM_SERVER_FORKED -> R.string.app_list_application_error_invalid_service_message_not_call_fork
//                    RemoteService.RCStatus.INJECT_FAILURE -> R.string.app_list_application_error_invalid_service_message_inject_failure
//                    RemoteService.RCStatus.SERVICE_NOT_CREATED -> R.string.app_list_application_error_invalid_service_message_service_not_created
//                    RemoteService.RCStatus.UNABLE_TO_HANDLE_REQUEST -> R.string.app_list_application_error_invalid_service_message_service_unable_to_handle
//                    RemoteService.RCStatus.SYSTEM_BLOCK_IPC -> R.string.app_list_application_error_invalid_service_message_system_block_ipc
//                    RemoteService.RCStatus.SERVICE_VERSION_NOT_MATCHES -> R.string.app_list_application_error_invalid_service_message_service_version_not_matches
//                    RemoteService.RCStatus.UNKNOWN -> R.string.app_list_application_error_invalid_service_message_unknown
//                }
//
//                AlertDialog.Builder(this)
//                        .setTitle(R.string.app_list_application_error_invalid_service_title)
//                        .setMessage(getString(resId)
//                                .split("\n").joinToString("\n", transform = String::trim))
//                        .setCancelable(false)
//                        .setPositiveButton(R.string.app_list_application_error_invalid_service_button_ok) { _, _ -> finish() }
//                        .show()
//            }
//            AppListController.ErrorType.UPDATE_FAILURE -> Snackbar.make(root, R.string.app_list_application_error_update_failure, Snackbar.LENGTH_LONG).show()
//            AppListController.ErrorType.NO_ANY_SUPPORT_APP -> Snackbar.make(root, R.string.app_list_application_error_empty_app_list, Snackbar.LENGTH_LONG).show();
//        }
//    }
}

