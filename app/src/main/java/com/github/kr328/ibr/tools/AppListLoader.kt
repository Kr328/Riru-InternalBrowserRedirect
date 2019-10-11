package com.github.kr328.ibr.tools

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.github.kr328.ibr.data.OnlineRules
import com.github.kr328.ibr.model.AppListElement
import com.github.kr328.ibr.remote.RemoteConnection

class AppListLoader(private val context: Context) {
    private val onlineRules = OnlineRules(context)

    fun load(cacheFirst: Boolean, ignoreCache: Boolean): List<AppListElement>? {
        return try {
            val pm = context.packageManager

            val enabled = RemoteConnection.connection.queryEnabledPackages().toSet()
            val data = onlineRules.queryRuleSets(cacheFirst, ignoreCache).packages
                    .map {
                        it to pm.getApplicationInfoOrNull(it.packageName)
                    }.filterNot {
                        it.second == null
                    }

            data.map {
                AppListElement(it.first.packageName,
                        it.second!!.loadLabel(pm).toString(),
                        AppListElement.AppRuleState(enabled.contains(it.first.packageName), AppListElement.RuleType.ONLINE),
                        it.second!!.loadIcon(pm))
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