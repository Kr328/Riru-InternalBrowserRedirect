package com.github.kr328.ibr.controller

import android.content.Context
import com.github.kr328.ibr.data.RedirectServiceData
import com.github.kr328.ibr.model.AppListData
import com.github.kr328.ibr.model.DataResult
import com.github.kr328.ibr.utils.SingleThreadPool

class AppListController(private val callback: Callback) {
    enum class Status {
        UNKNOWN, SUCCESS,
        REDIRECT_SERVICE_INVALID,
        ONLINE_RULE_UPDATE_FAILURE
    }

    interface Callback {
        fun getContext(): Context
        fun updateView(appListData: AppListData)
        fun onError(statusCode: Int)
    }

    fun refreshList(): Boolean {
        val context = callback.getContext()
        val packageManager = context.packageManager

        return singleThreadPool.execute {
            val result = RedirectServiceData.queryAllRuleSets()
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

    private val singleThreadPool = SingleThreadPool()
}