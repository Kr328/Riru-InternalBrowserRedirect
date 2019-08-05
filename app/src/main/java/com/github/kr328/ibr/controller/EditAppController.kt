package com.github.kr328.ibr.controller

import android.content.Context
import com.github.kr328.ibr.MainApplication
import com.github.kr328.ibr.data.RuleData
import com.github.kr328.ibr.data.state.RuleDataState
import com.github.kr328.ibr.data.state.RuleDataStateResult
import com.github.kr328.ibr.model.AppData
import java.util.concurrent.Executors

class EditAppController(private val context: Context,val pkg: String, private val callback: Callback) : RuleData.RuleDataCallback {
    interface Callback {
        fun showUpdating()
        fun closeUpdating()
        fun updateAppData(enabled: Boolean, appData: AppData)
        fun onError(error: ErrorType)
    }

    enum class ErrorType {
        UNKNOWN_APPLICATION, UPDATE_PACKAGES_FAILURE
    }

    private val ruleData = MainApplication.fromContext(context).ruleData
    private val executor = Executors.newSingleThreadExecutor()

    fun onStart() {
        ruleData.registerCallback(this)
        ruleData.requestPriority(pkg)

        updateView()
    }

    fun onStop() {
        ruleData.unregisterCallback(this)
    }

    fun setPackageEnabled(enabled: Boolean) {
        if ( enabled )
            ruleData.enablePackage(pkg)
        else
            ruleData.disablePackage(pkg)
    }

    override fun onStateChanged(state: RuleDataState) {
        when ( state ) {
            RuleDataState.IDLE -> callback.closeUpdating()
            RuleDataState.UPDATE_SINGLE_PACKAGE -> callback.showUpdating()
            else -> {}
        }
    }

    override fun onStateResult(result: RuleDataStateResult) {
        if ( result.state == RuleDataState.UPDATE_SINGLE_PACKAGE && result.success && result.packageName == pkg )
            updateView()
        else if ( result.state == RuleDataState.UPDATE_PACKAGES && !result.success )
            callback.onError(ErrorType.UPDATE_PACKAGES_FAILURE)
    }

    private fun updateView() {
        executor.submit {
            val pm = context.packageManager

            val info = pm.getPackageInfoOrNull(pkg)
            if ( info == null ) {
                callback.onError(ErrorType.UNKNOWN_APPLICATION)
                return@submit
            }

            val appData = AppData(pkg, info.applicationInfo.loadLabel(pm).toString(),
                    info.versionName, info.applicationInfo.loadIcon(pm), ruleData.queryPackage(pkg))

            callback.updateAppData(ruleData.isPackageEnabled(pkg), appData)
        }
    }
}