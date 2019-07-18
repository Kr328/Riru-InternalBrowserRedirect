package com.github.kr328.ibr.controller

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.github.kr328.ibr.data.DataResult
import com.github.kr328.ibr.data.RemoteData
import com.github.kr328.ibr.model.AppListData
import kotlin.concurrent.thread

class AppListAppController(private val callback: Callback) {
    interface Callback {
        fun getContext(): Context
        fun updateView(appListData: AppListData)
        fun onError(statusCode: Int)
    }

    fun refreshList(): Boolean {
        val context = callback.getContext()
        val packageManager = context.packageManager

        return execute {
            val result = RemoteData.queryAllRuleSets()
            val defaultIcon = context.resources.getDrawable(android.R.drawable.sym_def_app_icon, null)

            if (result.status == DataResult.STATUS_SUCCESS) {
                val applications = result.result
                        .mapValues { packageManager.getApplicationInfoOrNull(it.key) }
                        .map {
                            it.value?.run {
                                AppListData.Element(it.key, loadLabel(packageManager).toString(),
                                        AppListData.AppState(true, AppListData.AppState.RULE_TYPE_LOCAL), loadIcon(packageManager))
                            }
                                    ?: AppListData.Element(it.key, it.key, AppListData.AppState(true, AppListData.AppState.RULE_TYPE_LOCAL), defaultIcon)
                        }
                callback.updateView(AppListData(applications))
            } else {
                callback.onError(result.status)
            }
        }
    }

    @Synchronized
    private fun execute(runnable: () -> Unit): Boolean {
        return if (threadRunning)
            false
        else {
            threadRunning = true
            thread {
                runnable.invoke()
                synchronized(this@AppListAppController) {
                    threadRunning = false
                }
            }
            true
        }
    }

    private fun PackageManager.getApplicationInfoOrNull(packageName: String): ApplicationInfo? {
        return try {
            getApplicationInfo(packageName, 0)
        }
        catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    private var threadRunning = false
}