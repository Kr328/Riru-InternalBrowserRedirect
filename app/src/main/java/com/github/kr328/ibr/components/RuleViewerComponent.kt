package com.github.kr328.ibr.components

import android.app.Activity
import android.util.Log
import androidx.lifecycle.Transformations
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.MainApplication
import com.github.kr328.ibr.command.CommandChannel
import com.github.kr328.ibr.dialogs.RuleEditDialog
import com.github.kr328.ibr.model.AppInfoData
import java.util.concurrent.Executors

class RuleViewerComponent(private val application: MainApplication,
                          val packageName: String,
                          val type: String) {
    companion object {
        const val COMMAND_INITIAL_APP_INFO = "initial_app_info"
        const val COMMAND_CREATE_RULE = "create_rule"
    }

    data class RuleData(val index: Int, val tag: String, val urlSource: String)

    private val executor = Executors.newSingleThreadExecutor()

    val commandChannel = CommandChannel()
    val ruleSource by lazy {
        when (type) {
            "local" -> Transformations.map(application.database.ruleDao().observeLocalRuleForPackage(packageName)) { list ->
                list.map {
                    RuleData(it.index, it.tag, it.urlSource)
                }
            }
            "online" -> Transformations.map(application.database.ruleDao().observeOnlineRuleForPackage(packageName)) { list ->
                list.map {
                    RuleData(it.index, it.tag, it.urlSource)
                }
            }
            else -> throw IllegalArgumentException("Unsupported type $type")
        }
    }

    init {
        commandChannel.registerReceiver(COMMAND_CREATE_RULE) { _, context: Activity? ->
            if (context == null)
                return@registerReceiver

            executor.submit {
                val index = application.database.ruleDao().queryLocalRuleLatestIndexForPackage(packageName)
                        ?: 0

                context.runOnUiThread {
                    RuleEditDialog(context, type, packageName, index + 1).createAndShow()
                }
            }
        }

        executor.submit {
            try {
                val pm = application.packageManager
                val info = pm.getPackageInfo(packageName, 0)

                commandChannel.sendCommand(COMMAND_INITIAL_APP_INFO, AppInfoData(info.packageName,
                        info.applicationInfo.loadLabel(pm).toString(),
                        info.versionName,
                        info.applicationInfo.loadIcon(pm)))
            } catch (e: Exception) {
                Log.w(Constants.TAG, "Load application info failure", e)
            }
        }
    }

    fun shutdown() {
        executor.shutdown()
    }
}