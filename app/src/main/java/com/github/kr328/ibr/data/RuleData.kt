package com.github.kr328.ibr.data

import android.content.Context
import com.github.kr328.ibr.data.sources.LocalRepoSource
import com.github.kr328.ibr.data.sources.RemoteRepoSource
import com.github.kr328.ibr.data.state.RuleDataState
import com.github.kr328.ibr.data.state.RuleDataStateResult
import com.github.kr328.ibr.model.StoreRuleSet
import com.github.kr328.ibr.model.StoreRuleSets
import java.io.File

class RuleData(context: Context, cache: File, repo: RemoteRepoSource.RemoteRepo) {
    private val local = LocalRepoSource(cache)
    private val remote = RemoteRepoSource(repo)
    private val service = RemoteService()
    private val updater = RuleDataUpdater(context, service, local, remote)

    fun getServiceStatus(): RemoteService.RCStatus = service.getStatus()
    fun queryPreloadMetadata(): StoreRuleSets = StoreRuleSets(emptyList())
    fun queryLocalMetadata(): StoreRuleSets = local.queryAllPackages() ?: StoreRuleSets(emptyList())
    fun queryPackage(pkg: String): StoreRuleSet? = local.queryPackage(pkg)

    fun isPackageEnabled(pkg: String): Boolean = false
    fun enablePackage(pkg: String) = local.queryPackage(pkg)
    fun disablePackage(pkg: String) {}

    fun registerCallback(callback: RuleDataCallback) = updater.registerCallback(callback)
    fun unregisterCallback(callback: RuleDataCallback) = updater.unregisterCallback(callback)
    fun refresh(force: Boolean = false) = updater.refresh(force)
    fun currentState(): RuleDataState = updater.currentState

    interface RuleDataCallback {
        fun onStateChanged(state: RuleDataState)
        fun onStateResult(result: RuleDataStateResult)
    }
}
