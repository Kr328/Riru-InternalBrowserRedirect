package com.github.kr328.ibr.data

import android.content.Context
import com.github.kr328.ibr.data.sources.LocalRepoSource
import com.github.kr328.ibr.data.sources.RemoteRepoSource
import com.github.kr328.ibr.data.sources.ServiceSource
import com.github.kr328.ibr.data.state.RuleDataState
import com.github.kr328.ibr.data.state.RuleDataStateResult
import com.github.kr328.ibr.model.RuleSets
import com.github.kr328.ibr.model.RuleSet
import java.io.File

class RuleData(context: Context, cache: File, repo: RemoteRepoSource.RemoteRepo) {
    private val local = LocalRepoSource(cache)
    private val remote = RemoteRepoSource(repo)
    private val service = ServiceSource()
    private val updater = RuleDataUpdater(context, service, local, remote)

    fun getServiceStatus(): ServiceSource.RCStatus = service.getStatus()
    fun queryPreloadMetadata(): RuleSets = service.queryAllPackages() ?: RuleSets(emptyList())
    fun queryLocalMetadata(): RuleSets = local.queryAllPackages() ?: RuleSets(emptyList())
    fun queryPackage(pkg: String): RuleSet? = local.queryPackage(pkg) ?: service.queryPackage(pkg)

    fun isPackageEnabled(pkg: String): Boolean = service.queryPackage(pkg) != null
    fun enablePackage(pkg: String) = local.queryPackage(pkg)?.let { service.savePackage(pkg, it) }
    fun disablePackage(pkg: String) = service.removePackage(pkg)
    fun updateServiceFeature(feature: String, enabled: Boolean) = service.updateSetting(feature, enabled)

    fun registerCallback(callback: RuleDataCallback) = updater.registerCallback(callback)
    fun unregisterCallback(callback: RuleDataCallback) = updater.unregisterCallback(callback)
    fun refresh(force: Boolean = false) = updater.refresh(force)
    fun currentState(): RuleDataState = updater.currentState

    interface RuleDataCallback {
        fun onStateChanged(state: RuleDataState)
        fun onStateResult(result: RuleDataStateResult)
    }
}
