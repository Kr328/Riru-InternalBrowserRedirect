package com.github.kr328.ibr.controller

import android.content.ComponentCallbacks
import android.content.Context
import com.github.kr328.ibr.MainApplication
import com.github.kr328.ibr.data.RedirectServiceData
import com.github.kr328.ibr.data.RuleData
import com.github.kr328.ibr.data.RuleDataUpdater
import com.github.kr328.ibr.data.state.RuleDataState
import com.github.kr328.ibr.data.state.RuleDataStateResult
import com.github.kr328.ibr.model.AppListData
import com.github.kr328.ibr.model.DataResult
import com.github.kr328.ibr.utils.SingleThreadPool
import java.util.concurrent.Executors

class AppListController(private val context: Context, private val callback: Callback) : RuleData.RuleDataCallback {
    enum class ErrorType {

    }

    interface Callback {
        fun showProgress()
        fun closeProgress()
        fun updateAppList(data: AppListData)
        fun onError(error: ErrorType)
    }

    private val application = MainApplication.fromContext(context)
    private val executor = Executors.newSingleThreadExecutor()

    override fun onStateChanged(state: RuleDataState) {
        if ( application.ruleData.currentState() == RuleDataState.IDLE )
            callback.closeProgress()
        else
            callback.showProgress()
    }

    override fun onStateResult(result: RuleDataStateResult) {
        if ( result.state == RuleDataState.UPDATE_PACKAGES && result.success )
            updateList()
    }

    fun forceRefresh() {
        application.ruleData.refresh(true)
    }

    fun onStart() {
        application.ruleData.registerCallback(this)

        if ( application.ruleData.currentState() == RuleDataState.IDLE )
            callback.closeProgress()
        else
            callback.showProgress()

        application.ruleData.refresh()
    }

    fun onStop() {
        application.ruleData.unregisterCallback(this)
    }

    private fun updateList() {
        executor.submit {

        }
    }
}