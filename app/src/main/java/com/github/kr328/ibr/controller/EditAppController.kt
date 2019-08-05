package com.github.kr328.ibr.controller

import android.content.Context
import com.github.kr328.ibr.data.RedirectServiceData
import com.github.kr328.ibr.model.AppData
import com.github.kr328.ibr.model.DataResult
import com.github.kr328.ibr.utils.SingleThreadPool

class EditAppController(private val callback: Callback) {
    interface Callback {
        fun getContext(): Context
        fun updateAppData(appData: AppData)
        fun onError(status: Int)
    }

    fun refresh(pkg: String): Boolean {
        val packageManager = callback.getContext().packageManager

        return singleThreadPool.execute {
            packageManager.getPackageInfoOrNull(pkg)?.also { packageInfo ->
                RedirectServiceData.queryAppRuleSet(pkg)
                        .takeIf { dataResult -> dataResult.status == DataResult.STATUS_SUCCESS }?.also {
                            callback.updateAppData(AppData(packageInfo.packageName, packageInfo.applicationInfo.loadLabel(packageManager).toString(),
                                    packageInfo.versionName, packageInfo.applicationInfo.loadIcon(packageManager), it.result!!))
                        } ?: callback.onError(2)
            } ?: callback.onError(1)
        }
    }

    private val singleThreadPool = SingleThreadPool()
}