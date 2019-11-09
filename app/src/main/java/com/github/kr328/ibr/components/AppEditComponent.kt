package com.github.kr328.ibr.components

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.MainApplication
import com.github.kr328.ibr.command.CommandChannel
import com.github.kr328.ibr.model.AppEditData
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class AppEditComponent(private val application: MainApplication,
                       val packageName: String) {
    private val executor = Executors.newSingleThreadExecutor()

    val commandChannel = CommandChannel()
    val onlineRuleSet = application.database.ruleSetDao().observerOnlineRuleSet(packageName)
    val onlineRuleCount = application.database.ruleDao().observeOnlineRuleCount(packageName)
    val localRuleCount = application.database.ruleDao().observeLocalRuleCount(packageName)
    val appDate = MutableLiveData<AppEditData>()

    init {
        thread {
            try {
                val pm = application.packageManager
                val info = pm.getPackageInfo(packageName, 0)

                appDate.postValue(AppEditData(info.packageName,
                        info.applicationInfo.loadLabel(pm).toString(),
                        info.versionName,
                        info.applicationInfo.loadIcon(pm)))
            }
            catch (e: Exception) {
                Log.w(Constants.TAG, "Load application info failure", e)
            }
        }
    }
}