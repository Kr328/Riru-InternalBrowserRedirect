package com.github.kr328.ibr.components

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.MainApplication
import com.github.kr328.ibr.adapters.AppItemAdapter
import com.github.kr328.ibr.command.CommandChannel
import com.github.kr328.ibr.data.LocalRuleSetEntity
import com.github.kr328.ibr.data.OnlineRuleEntity
import com.github.kr328.ibr.data.OnlineRuleSetEntity
import com.github.kr328.ibr.data.OutOfDateEntity
import com.github.kr328.ibr.model.AppListElement
import com.github.kr328.ibr.model.RuleSetsStore
import java.util.concurrent.Executors
import kotlin.streams.toList

class AppListComponent(private val application: MainApplication) {
    companion object {
        const val COMMAND_REFRESH_ONLINE_RULES = "refresh_online_rules"
        const val COMMAND_SHOW_REFRESHING = "show_refreshing"
        const val COMMAND_SHOW_ADD_RULE_SET = "show_add_rule_set"
        const val COMMAND_SHOW_EXCEPTION = "show_exception"
    }

    enum class ExceptionType {
        QUERY_DATA_FAILURE,
        REFRESH_FAILURE
    }

    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler()

    val elements: MediatorLiveData<List<AppListElement>> = MediatorLiveData()
    val commandChannel = CommandChannel()

    init {
        val local = application.database.ruleSetDao().observeLocalRuleSets()
        val online = application.database.ruleSetDao().observeOnlineRuleSets()

        elements.addSource(local) {
            postLoadAppList(local = local.value, online = online.value)
        }
        elements.addSource(online) {
            postLoadAppList(local = local.value, online = online.value)
        }
        elements.addSource(application.remoteService.enabledPackages) {
            postLoadAppList(local = local.value, online = online.value)
        }

        commandChannel.registerReceiver(COMMAND_REFRESH_ONLINE_RULES) { _, force: Boolean? ->
            executor.submit {
                refreshOnlineRuleSet(force ?: false)
            }
        }

        commandChannel.registerReceiver(COMMAND_SHOW_ADD_RULE_SET) { _, activity: Activity? ->
            if (activity == null)
                return@registerReceiver

            executor.submit {
                val pm = activity.packageManager

                val result = pm.getInstalledApplications(0)
                        .filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 }
                        .map { AppItemAdapter.Item(it.loadLabel(pm).toString(), it.packageName) }
                        .sortedBy { it.name }

                activity.runOnUiThread {
                    AlertDialog.Builder(activity)
                            .setAdapter(AppItemAdapter(activity, result)) { d, index ->
                                createLocalRuleSet(result[index].packageName)
                                d.dismiss()
                            }
                            .show()
                }
            }
        }
    }

    fun shutdown() {
        executor.shutdown()
    }

    private fun postLoadAppList(local: List<LocalRuleSetEntity>?, online: List<OnlineRuleSetEntity>?) {
        handler.removeMessages(0)
        handler.postDelayed({
            executor.submit {
                loadAppList(local = local, online = online)
            }
        }, 250)
    }

    private fun loadAppList(local: List<LocalRuleSetEntity>?, online: List<OnlineRuleSetEntity>?) {
        commandChannel.sendCommand(COMMAND_SHOW_REFRESHING, true, 250)

        try {
            val pm = application.packageManager

            val enabledPackages = application.remoteService.enabledPackages.value
                    ?: emptySet<String>()
            val localPackages = local?.map(LocalRuleSetEntity::packageName)?.toSet() ?: emptySet()
            val onlinePackages = online?.map(OnlineRuleSetEntity::packageName)?.toSet()
                    ?: emptySet()

            val packages = enabledPackages.union(localPackages).union(onlinePackages)

            val dao = application.database.ruleDao()

            val result = packages.mapNotNull { pm.getApplicationInfoOrNull(it) }
                    .map {
                        val localRules = dao.queryLocalRulesForPackage(it.packageName)
                        val onlineRules = dao.queryOnlineRulesForPackage(it.packageName)

                        AppListElement(enabledPackages.contains(it.packageName),
                                it.packageName,
                                it.loadLabel(pm).toString(),
                                localRules.size + onlineRules.size,
                                it.loadIcon(pm))
                    }
                    .sortedWith(compareByDescending(AppListElement::enable).thenBy(AppListElement::name))

            elements.postValue(result)
        } catch (e: Exception) {
            commandChannel.sendCommand(COMMAND_SHOW_EXCEPTION, ExceptionType.QUERY_DATA_FAILURE)
            Log.w(Constants.TAG, "Load data failure", e)
        }

        commandChannel.cancelCommand(COMMAND_SHOW_REFRESHING)
        commandChannel.sendCommand(COMMAND_SHOW_REFRESHING, false)
    }

    private fun refreshOnlineRuleSet(force: Boolean) {
        if (!force && (application.database.outOfDateDao().queryOutOfDate("set:online_rule_set")
                        ?: -1L) > System.currentTimeMillis()) return

        commandChannel.sendCommand(COMMAND_SHOW_REFRESHING, true)

        try {
            val ruleSet = application.onlineRuleRepo.queryRuleSets()

            val targetPackages = application.packageManager.getInstalledPackages(0).map(PackageInfo::packageName).toSet()
                    .intersect(ruleSet.packages.map(RuleSetsStore.Data::packageName).toSet())

            val ruleSetDao = application.database.ruleSetDao()
            val ruleDao = application.database.ruleDao()

            val currentPackages = ruleSetDao.getOnlineRuleSets().map(OnlineRuleSetEntity::packageName).toSet()
            val removePackages = currentPackages - targetPackages
            val newPackages = targetPackages - currentPackages


            val newRuleSets = newPackages.parallelStream().map { pkg ->
                try {
                    pkg to application.onlineRuleRepo.queryRuleSet(pkg)
                } catch (e: Exception) {
                    Log.w(Constants.TAG, "Download $pkg rule failure")
                    null
                }
            }.toList().filterNotNull()

            application.database.runInTransaction {
                ruleSetDao.addOnlineRuleSets(newRuleSets.map {
                    OnlineRuleSetEntity(it.first, it.second.tag, it.second.authors)
                })
                ruleSetDao.removeOnlineRuleSets(removePackages)

                newRuleSets.flatMap {
                    it.second.rules.mapIndexed { index, rule ->
                        OnlineRuleEntity(it.first,
                                index,
                                rule.tag,
                                rule.urlSource,
                                rule.urlFilters.ignore,
                                rule.urlFilters.force)
                    }
                }.apply(ruleDao::saveAllOnlineRules)

                application.database.outOfDateDao()
                        .saveOutOfDate(OutOfDateEntity("set:online_rule_set",
                                System.currentTimeMillis() + Constants.DEFAULT_RULE_INVALIDATE))
            }
        } catch (e: Exception) {
            commandChannel.sendCommand(COMMAND_SHOW_EXCEPTION, ExceptionType.REFRESH_FAILURE)
            Log.w(Constants.TAG, "Update rule set failure", e)
        }

        commandChannel.sendCommand(COMMAND_SHOW_REFRESHING, false)
    }

    private fun createLocalRuleSet(packageName: String) {
        executor.submit {
            application.database.ruleSetDao().addLocalRuleSet(LocalRuleSetEntity(packageName))
        }
    }

    private fun PackageManager.getApplicationInfoOrNull(packageName: String): ApplicationInfo? {
        return try {
            this.getApplicationInfo(packageName, 0)
        } catch (e: Exception) {
            null
        }
    }
}