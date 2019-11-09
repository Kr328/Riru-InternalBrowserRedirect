package com.github.kr328.ibr.components

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.MainApplication
import com.github.kr328.ibr.command.CommandChannel
import com.github.kr328.ibr.data.*
import com.github.kr328.ibr.model.AppListElement
import com.github.kr328.ibr.model.RuleSetsStore
import com.github.kr328.ibr.remote.RemoteConnection
import java.util.concurrent.Executors
import java.util.stream.Collectors
import kotlin.streams.toList

class AppListComponent(private val application: MainApplication) {
    companion object {
        const val REFRESH_ONLINE_RULES = "refresh_online_rules"
        const val SHOW_REFRESHING = "show_refreshing"
    }

    private val executor = Executors.newSingleThreadExecutor()

    val elements: MediatorLiveData<List<AppListElement>> = MediatorLiveData()
    val commandChannel = CommandChannel()

    init {
        val local = application.database.ruleSetDao().observeLocalRuleSets()
        val online = application.database.ruleSetDao().observeOnlineRuleSets()

        elements.addSource(local) {
            executor.submit {
                loadAppList(local = local.value, online = online.value)
            }
            return@addSource
        }

        commandChannel.registerReceiver(REFRESH_ONLINE_RULES) { _, force: Boolean? ->
            executor.submit {
                refreshOnlineRuleSet(force ?: false)
            }
        }
    }

    private fun loadAppList(local: List<LocalRuleSetEntity>?, online: List<OnlineRuleSetEntity>?) {
        commandChannel.sendCommand(SHOW_REFRESHING, true, 250)

        try {
            val pm = application.packageManager

            val enabledPackages = RemoteConnection.connection.queryEnabledPackages().toSet()
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

            elements.postValue(result)
        } catch (e: Exception) {
            Log.w(Constants.TAG, "Load data failure", e)
        }

        commandChannel.cancelCommand(SHOW_REFRESHING)
        commandChannel.sendCommand(SHOW_REFRESHING, false)
    }

    private fun refreshOnlineRuleSet(force: Boolean) {
        if (!force && (application.database.outOfDateDao().queryOutOfDate("set:online_rule_set")
                        ?: -1L) > System.currentTimeMillis()) return

        commandChannel.sendCommand(SHOW_REFRESHING, true)

        try {
            val ruleSet = application.onlineRuleRemote.queryRuleSets(cacheFirst = false, ignoreCache = true)

            val targetPackages = application.packageManager.getInstalledPackages(0).map(PackageInfo::packageName).toSet()
                    .intersect(ruleSet.packages.map(RuleSetsStore.Data::packageName).toSet())

            val ruleSetDao = application.database.ruleSetDao()
            val ruleDao = application.database.ruleDao()

            val currentPackages = ruleSetDao.getOnlineRuleSets().map(OnlineRuleSetEntity::packageName).toSet()
            val removePackages = currentPackages - targetPackages
            val newPackages = targetPackages - currentPackages

            ruleSetDao.removeOnlineRuleSets(removePackages)
            newPackages.parallelStream().map { pkg ->
                try {
                    pkg to application.onlineRuleRemote.queryRuleSet(pkg, cacheFirst = false, ignoreCache = true)
                } catch (e: Exception) {
                    Log.w(Constants.TAG, "Download $pkg rule failure")
                    null
                }
            }.filter {
                it != null
            }.map {
                it!!
            }.peek {
                ruleSetDao.addOnlineRuleSet(OnlineRuleSetEntity(it.first, it.second.tag, it.second.authors))
            }.flatMap {
                it.second.rules.mapIndexed { index, rule ->
                    OnlineRuleEntity(it.first,
                            index,
                            rule.tag,
                            rule.urlSource.toString(),
                            rule.urlFilters.ignore.pattern,
                            rule.urlFilters.force.pattern)
                }.stream()
            }.toList().apply(ruleDao::saveAllOnlineRules)

            application.database.outOfDateDao()
                    .saveOutOfDate(OutOfDateEntity("set:online_rule_set",
                            System.currentTimeMillis() + Constants.DEFAULT_RULE_INVALIDATE))
        } catch (e: Exception) {
            Log.w(Constants.TAG, "Update rule set failure", e)
        }

        commandChannel.sendCommand(SHOW_REFRESHING, false)
    }

    private fun PackageManager.getApplicationInfoOrNull(packageName: String): ApplicationInfo? {
        return try {
            this.getApplicationInfo(packageName, 0)
        } catch (e: Exception) {
            null
        }
    }
}