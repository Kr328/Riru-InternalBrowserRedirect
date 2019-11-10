package com.github.kr328.ibr.components

import android.net.Uri
import android.util.Log
import androidx.lifecycle.Transformations
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.MainApplication
import com.github.kr328.ibr.command.CommandChannel
import com.github.kr328.ibr.model.AppInfoData
import com.github.kr328.ibr.model.RuleSetStore
import java.util.concurrent.Executors

class RuleViewerComponent(private val application: MainApplication,
                          private val packageName: String,
                          val type: String) {
    companion object {
        const val COMMAND_INITIAL_APP_INFO = "initial_app_info"
    }

    private val executor = Executors.newSingleThreadExecutor()

    val commandChannel = CommandChannel()
    val ruleSource by lazy {
        when (type) {
            "local" -> Transformations.map(application.database.ruleDao().observeLocalRuleForPackage(packageName)) { list ->
                list.map {
                    RuleSetStore.Rule(it.tag,
                            Uri.parse(it.urlSource),
                            RuleSetStore.UrlFilters(Regex(it.urlIgnore), Regex(it.urlForce)))
                }
            }
            "online" -> Transformations.map(application.database.ruleDao().observeOnlineRuleForPackage(packageName)) { list ->
                list.map {
                    RuleSetStore.Rule(it.tag,
                            Uri.parse(it.urlSource),
                            RuleSetStore.UrlFilters(Regex(it.urlIgnore), Regex(it.urlForce)))
                }
            }
            else -> throw IllegalArgumentException("Unsupported type $type")
        }
    }

    init {
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
}