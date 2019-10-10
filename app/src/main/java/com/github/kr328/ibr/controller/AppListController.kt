package com.github.kr328.ibr.controller

import android.content.Context
import com.github.kr328.ibr.MainApplication
import com.github.kr328.ibr.data.RuleData
import com.github.kr328.ibr.data.sources.ServiceSource
import com.github.kr328.ibr.data.state.RuleDataState
import com.github.kr328.ibr.data.state.RuleDataStateResult
import com.github.kr328.ibr.model.AppListElement
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class AppListController(private val context: Context, private val callback: Callback) : RuleData.RuleDataCallback {
    enum class ErrorType {
        INVALID_SERVICE, UPDATE_FAILURE, NO_ANY_SUPPORT_APP
    }

    interface Callback {
        fun showProgress()
        fun closeProgress()
        fun updateAppList(data: AppListElement)
        fun onError(error: ErrorType, extras: Any)
    }

    private val ruleData = MainApplication.fromContext(context).ruleData
    private val executor = Executors.newSingleThreadExecutor()
    private val running = AtomicBoolean(false)

    override fun onStateChanged(state: RuleDataState) {
        updateProgress()
    }

    override fun onStateResult(result: RuleDataStateResult) {
        if (result.state == RuleDataState.UPDATE_PACKAGES) {
            updateList()

            if (!result.success)
                callback.onError(ErrorType.UPDATE_FAILURE, Any())
            else if ( ruleData.queryLocalMetadata().packages.isEmpty() )
                callback.onError(ErrorType.NO_ANY_SUPPORT_APP, Any())
        }
    }

    fun forceRefresh() {
        ruleData.refresh(true)
    }

    fun onStart() {
        if ( ruleData.getServiceStatus() != ServiceSource.RCStatus.RUNNING ) {
            callback.onError(ErrorType.INVALID_SERVICE, ruleData.getServiceStatus())
            return
        }

        ruleData.registerCallback(this)

        updateList()
        updateProgress()

        ruleData.refresh()
    }

    fun onStop() {
        ruleData.unregisterCallback(this)
    }

    private fun updateList() {
        executor.submit {
            running.set(true)

            val pm = context.packageManager
            val local = ruleData.queryLocalMetadata().packages.map { it.packageName to it }.toMap()
            val preload = ruleData.queryPreloadMetadata().packages.map { it.packageName to it }.toMap()

            val elements = (preload + local).mapNotNull {
                val info = pm.getApplicationInfoOrNull(it.key)

                if (info != null) {
                    val l = local[it.key]
                    val p = preload[it.key]

                    when {
                        l != null && p != null -> AppListElement.Element(info.packageName, info.loadLabel(pm).toString(),
                                AppListElement.AppRuleState(true, AppListElement.RuleType.ONLINE), info.loadIcon(pm))
                        l != null -> AppListElement.Element(info.packageName, info.loadLabel(pm).toString(),
                                AppListElement.AppRuleState(false, AppListElement.RuleType.ONLINE), info.loadIcon(pm))
                        p != null -> AppListElement.Element(info.packageName, info.loadLabel(pm).toString(),
                                AppListElement.AppRuleState(true, AppListElement.RuleType.PRELOAD), info.loadIcon(pm))
                        else -> null
                    }
                } else {
                    val p = preload[it.key]

                    when {
                        p != null -> AppListElement.Element(it.key, it.key,
                                AppListElement.AppRuleState(true, AppListElement.RuleType.PRELOAD), context.getDrawable(android.R.drawable.sym_def_app_icon)!!)
                        else -> null
                    }
                }
            }

            callback.updateAppList(AppListElement(elements.sortedWith(compareBy({ it.appState.getPriority() }, { it.name }))))

            running.set(false)

            updateProgress()
        }
    }

    private fun AppListElement.AppRuleState.getPriority(): Int {
        val rulePriority = when (ruleType) {
            AppListElement.RuleType.ONLINE -> 0
            AppListElement.RuleType.PRELOAD -> 1
            AppListElement.RuleType.UNKNOWN -> 2
        }

        return if (enabled)
            rulePriority - 10000
        else
            rulePriority
    }

    private fun updateProgress() {
        if ( ruleData.currentState() == RuleDataState.IDLE && !running.get() )
            callback.closeProgress()
        else
            callback.showProgress()
    }
}