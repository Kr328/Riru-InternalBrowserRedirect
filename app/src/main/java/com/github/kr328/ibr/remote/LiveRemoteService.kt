package com.github.kr328.ibr.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.remote.shared.RuleSet
import kotlin.concurrent.thread

class LiveRemoteService {
    val enabledPackages = MutableLiveData<List<String>>()

    init {
        thread {
            try {
                enabledPackages.postValue(RemoteConnection.connection.queryEnabledPackages().toList())
            }
            catch (e: Exception) {
                Log.w(Constants.TAG, "Initial enabled package failure", e)
            }
        }
    }

    fun updateRuleSet(packageName: String, ruleSet: RuleSet) {
        RemoteConnection.connection.updateRuleSet(packageName, ruleSet)

        enabledPackages.postValue(RemoteConnection.connection.queryEnabledPackages().toList())
    }

    fun removeRuleSet(packageName: String) {
        RemoteConnection.connection.removeRuleSet(packageName)

        enabledPackages.postValue(RemoteConnection.connection.queryEnabledPackages().toList())
    }

    fun queryRuleSet(packageName: String): RuleSet? {
        return RemoteConnection.connection.queryRuleSet(packageName)
    }
}