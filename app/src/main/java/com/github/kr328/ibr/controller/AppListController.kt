package com.github.kr328.ibr.controller

import android.content.Context
import com.github.kr328.ibr.MainApplication
import com.github.kr328.ibr.R
import com.github.kr328.ibr.data.RuleData
import com.github.kr328.ibr.data.state.RuleDataState
import com.github.kr328.ibr.data.state.RuleDataStateResult
import com.github.kr328.ibr.model.AppListData
import java.util.concurrent.Executors

class AppListController(private val context: Context, private val callback: Callback) : RuleData.RuleDataCallback {
    enum class ErrorType {
        INVALID_SERVICE, UPDATE_FAILURE
    }

    interface Callback {
        fun showProgress()
        fun closeProgress()
        fun updateAppList(data: AppListData)
        fun onError(error: ErrorType)
    }

    private val ruleData = MainApplication.fromContext(context).ruleData
    private val executor = Executors.newSingleThreadExecutor()

    override fun onStateChanged(state: RuleDataState) {
        if ( state == RuleDataState.UPDATE_PACKAGES )
            callback.showProgress()
        else
            callback.closeProgress()
    }

    override fun onStateResult(result: RuleDataStateResult) {
        if (result.state == RuleDataState.UPDATE_PACKAGES) {
            updateList()

            if ( !result.success )
                callback.onError(ErrorType.UPDATE_FAILURE)
        }
    }

    fun forceRefresh() {
        ruleData.refresh(true)
    }

    fun onStart() {
        if ( !ruleData.isValidService() ) {
            callback.onError(ErrorType.INVALID_SERVICE)

            return
        }

        ruleData.registerCallback(this)

        if ( ruleData.currentState() == RuleDataState.IDLE )
            callback.closeProgress()
        else
            callback.showProgress()

        ruleData.refresh()
    }

    fun onStop() {
        ruleData.unregisterCallback(this)
    }

    private fun updateList() {
        executor.submit {
            val pm = context.packageManager
            val local = ruleData.queryLocalMetadata().packages.map { it.packageName to it }.toMap()
            val preload = ruleData.queryPreloadMetadata().packages.map { it.packageName to it }.toMap()

            val elements = (preload + local).mapNotNull {
                val info = pm.getApplicationInfoOrNull(it.key)

                if ( info != null ) {
                    val l = local[it.key]
                    val p = preload[it.key]

                    when {
                        l != null && p != null -> AppListData.Element(info.packageName, info.loadLabel(pm).toString(),
                                AppListData.AppState(true, AppListData.RuleType.ONLINE), info.loadIcon(pm))
                        l != null -> AppListData.Element(info.packageName, info.loadLabel(pm).toString(),
                                AppListData.AppState(false, AppListData.RuleType.ONLINE), info.loadIcon(pm))
                        p != null -> AppListData.Element(info.packageName, info.loadLabel(pm).toString(),
                                AppListData.AppState(true, AppListData.RuleType.PRELOAD), info.loadIcon(pm))
                        else -> null
                    }
                }
                else {
                    val l = local[it.key]
                    val p = preload[it.key]

                    when {
                        l != null && p != null -> AppListData.Element(it.key, it.key,
                                AppListData.AppState(true, AppListData.RuleType.ONLINE), context.getDrawable(R.drawable.ic_unknown_app)!!)
                        p != null -> AppListData.Element(it.key, it.key,
                                AppListData.AppState(true, AppListData.RuleType.PRELOAD), context.getDrawable(R.drawable.ic_unknown_app)!!)
                        else -> null
                    }
                }
            }

            callback.updateAppList(AppListData(elements.sortedWith(compareBy({it.appState.getPriority()}, { it.name }))))
        }
    }

    private fun AppListData.AppState.getPriority(): Int {
        return if ( enabled ) {
            10000 + when ( ruleType ) {
                AppListData.RuleType.ONLINE -> 2
                AppListData.RuleType.PRELOAD -> 1
                AppListData.RuleType.UNKNOWN -> 0
            }
        }
        else {
            when ( ruleType ) {
                AppListData.RuleType.ONLINE -> 2
                AppListData.RuleType.PRELOAD -> 1
                AppListData.RuleType.UNKNOWN -> 0
            }
        }
    }
}