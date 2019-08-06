package com.github.kr328.ibr.data

import android.content.Context
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.data.sources.LocalRepoSource
import com.github.kr328.ibr.data.sources.RemoteRepoSource
import com.github.kr328.ibr.data.sources.ServiceSource
import com.github.kr328.ibr.data.state.RuleDataState
import com.github.kr328.ibr.data.state.RuleDataStateResult
import com.github.kr328.ibr.model.Packages
import com.github.kr328.ibr.model.RuleSet
import com.github.kr328.ibr.utils.ExceptionUtils
import java.io.File

class RuleData(context: Context, cache: File, repo: RemoteRepoSource.RemoteRepo) {
    private val local = LocalRepoSource(cache)
    private val remote = RemoteRepoSource(repo)
    private val service = ServiceSource()
    private val updater = RuleDataUpdater(context, service, local, remote)

    fun isValidService(): Boolean = ExceptionUtils.fallback({ service.connection.version == Constants.REMOTE_SERVICE_VERSION }, false)
    fun queryPreloadMetadata(): Packages = service.queryAllPackages() ?: Packages(emptyList())
    fun queryLocalMetadata(): Packages = local.queryAllPackages() ?: Packages(emptyList())
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
