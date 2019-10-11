package com.github.kr328.ibr.tools

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

import com.github.kr328.ibr.data.LocalRules
import com.github.kr328.ibr.data.OnlineRules
import com.github.kr328.ibr.model.AppListElement
import com.github.kr328.ibr.model.StoreRuleSets
import com.github.kr328.ibr.remote.RemoteConnection

class AppListLoader(private val context: Context) {
    private val onlineRules = OnlineRules(context)
    private val localRules = LocalRules(context)

    fun load(cacheFirst: Boolean, ignoreCache: Boolean): List<AppListElement>? {
        return try {
            val pm = context.packageManager

            val enabled = RemoteConnection.connection.queryEnabledPackages().toSet()
            val local = localRules.queryRuleSets().packages.map(StoreRuleSets.Data::packageName).toSet()
            val online = onlineRules.queryRuleSets(cacheFirst, ignoreCache)
                    .packages.map(StoreRuleSets.Data::packageName).toSet()

            val packages = enabled.union(local).union(online)

            packages.mapNotNull { pm.getApplicationInfoOrNull(it) }
                    .map {
                        val status = mutableSetOf<AppListElement.RuleStatus>()

                        if ( enabled.contains(it.packageName) ) {
                            status += AppListElement.RuleStatus.PRELOAD
                            status += AppListElement.RuleStatus.ENABLED
                        }
                        if ( local.contains(it.packageName) )
                            status += AppListElement.RuleStatus.LOCAL
                        if ( online.contains(it.packageName) )
                            status += AppListElement.RuleStatus.ONLINE

                        AppListElement(it.packageName,
                                it.loadLabel(pm).toString(),
                                status,
                                it.loadIcon(pm))
                    }
        } catch (e: Exception) {
            null
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