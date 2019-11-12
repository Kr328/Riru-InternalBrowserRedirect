package com.github.kr328.ibr.components

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.MainApplication
import com.github.kr328.ibr.command.CommandChannel
import com.github.kr328.ibr.data.OnlineRuleEntity
import com.github.kr328.ibr.data.OnlineRuleSetEntity
import com.github.kr328.ibr.model.AppInfoData
import com.github.kr328.ibr.remote.shared.Rule
import com.github.kr328.ibr.remote.shared.RuleSet
import java.util.concurrent.Executors

class AppEditComponent(private val application: MainApplication,
                       val packageName: String) {
    companion object {
        const val COMMAND_INITIAL_FEATURE_ENABLED = "initial_feature_enabled"
        const val COMMAND_SET_DEBUG_ENABLED = "set_debug_enabled"
        const val COMMAND_SET_ONLINE_ENABLED = "set_online_enabled"
        const val COMMAND_SET_LOCAL_ENABLED = "set_local_enabled"
        const val COMMAND_REMOVE_LOCAL_RULE_SET = "remove_local_rule_set"
        const val COMMAND_SHOW_EXCEPTION = "show_exception"
    }

    enum class ExceptionType {
        LOAD_APP_INFO_FAILURE,
        QUERY_DATA_FROM_SERVICE_FAILURE,
        REFRESH_FAILURE,
        PUSH_DATA_TO_SERVICE_FAILURE
    }

    data class FeatureEnabled(val debug: Boolean, val online: Boolean, val local: Boolean)

    private val executor = Executors.newSingleThreadExecutor()

    private var featureEnabled = FeatureEnabled(debug = false, online = false, local = false)

    val commandChannel = CommandChannel()
    val onlineRuleSet = application.database.ruleSetDao().observerOnlineRuleSet(packageName)
    val onlineRuleCount = application.database.ruleDao().observeOnlineRuleCount(packageName)
    val localRuleCount = application.database.ruleDao().observeLocalRuleCount(packageName)
    val appDate = MutableLiveData<AppInfoData>()

    private val foreverObserver = Observer<Int> {
        executor.submit {
            updateRemoteServiceData()
        }
    }

    init {
        executor.submit {
            try {
                val pm = application.packageManager
                val info = pm.getPackageInfo(packageName, 0)

                appDate.postValue(AppInfoData(info.packageName,
                        info.applicationInfo.loadLabel(pm).toString(),
                        info.versionName,
                        info.applicationInfo.loadIcon(pm)))
            } catch (e: Exception) {
                commandChannel.sendCommand(COMMAND_SHOW_EXCEPTION, ExceptionType.LOAD_APP_INFO_FAILURE)
                Log.w(Constants.TAG, "Load application info failure", e)
            }
        }
        executor.submit {
            try {
                val remote = application.remoteService.queryRuleSet(packageName) ?: return@submit

                featureEnabled = FeatureEnabled(remote.debug,
                        remote.extras.contains("online"),
                        remote.extras.contains("local"))

                commandChannel.sendCommand(COMMAND_INITIAL_FEATURE_ENABLED, featureEnabled)
            } catch (e: Exception) {
                commandChannel.sendCommand(COMMAND_SHOW_EXCEPTION, ExceptionType.QUERY_DATA_FROM_SERVICE_FAILURE)
                Log.w(Constants.TAG, "Unable to get remoteService info", e)
            }
        }

        localRuleCount.observeForever(foreverObserver)

        commandChannel.registerReceiver(COMMAND_SET_DEBUG_ENABLED, this::setFeatureEnabled)
        commandChannel.registerReceiver(COMMAND_SET_LOCAL_ENABLED, this::setFeatureEnabled)
        commandChannel.registerReceiver(COMMAND_SET_ONLINE_ENABLED, this::setFeatureEnabled)

        commandChannel.registerReceiver(COMMAND_REMOVE_LOCAL_RULE_SET) { _, _: String? ->
            executor.submit {
                application.database.ruleSetDao().removeLocalRuleSet(packageName)
            }
        }
    }

    fun shutdown() {
        localRuleCount.removeObserver(foreverObserver)
        executor.shutdown()
    }

    private fun setFeatureEnabled(command: String, enabled: Boolean?) {
        if (enabled == null)
            return

        when (command) {
            COMMAND_SET_DEBUG_ENABLED ->
                featureEnabled = featureEnabled.copy(debug = enabled)
            COMMAND_SET_ONLINE_ENABLED ->
                featureEnabled = featureEnabled.copy(online = enabled)
            COMMAND_SET_LOCAL_ENABLED ->
                featureEnabled = featureEnabled.copy(local = enabled)
        }

        updateRemoteServiceData()
    }

    private fun updateRemoteServiceData() {
        executor.submit {
            try {
                if (!featureEnabled.online && !featureEnabled.local && !featureEnabled.debug) {
                    application.remoteService.removeRuleSet(packageName)
                    return@submit
                }

                val ruleSet = RuleSet()

                ruleSet.debug = featureEnabled.debug

                if (featureEnabled.local) {
                    ruleSet.rules.addAll(application.database.ruleDao()
                            .queryLocalRulesForPackage(packageName)
                            .map {
                                Rule().apply {
                                    tag = it.tag
                                    urlPath = Uri.parse(it.urlSource)
                                    regexIgnore = it.urlIgnore
                                    regexForce = it.urlForce
                                }
                            })
                    ruleSet.extras.add("local")
                }

                if (featureEnabled.online) {
                    ruleSet.rules.addAll(application.database.ruleDao()
                            .queryOnlineRulesForPackage(packageName)
                            .map {
                                Rule().apply {
                                    tag = it.tag
                                    urlPath = Uri.parse(it.urlSource)
                                    regexIgnore = it.urlIgnore
                                    regexForce = it.urlForce
                                }
                            })
                    ruleSet.extras.add("online")
                }

                application.remoteService.updateRuleSet(packageName, ruleSet)
            } catch (e: Exception) {
                commandChannel.sendCommand(COMMAND_SHOW_EXCEPTION, ExceptionType.PUSH_DATA_TO_SERVICE_FAILURE)
                Log.w(Constants.TAG, "Update remoteService failure", e)
            }
        }
    }
}