package com.github.kr328.ibr.data

import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.data.sources.LocalRepoSource
import com.github.kr328.ibr.data.sources.RemoteRepoSource
import com.github.kr328.ibr.data.sources.ServiceSource
import com.github.kr328.ibr.data.state.RuleDataState
import com.github.kr328.ibr.data.state.RuleDataStateResult
import com.github.kr328.ibr.model.PackageRuleSet
import com.github.kr328.ibr.model.PackagesMetadata
import com.github.kr328.ibr.remote.openRemoteConnection
import com.github.kr328.ibr.utils.ExceptionUtils
import java.io.File

class RuleData(cache: File, user: String, repo: String) {
    private val local = LocalRepoSource(cache)
    private val remote = RemoteRepoSource(user, repo)
    private val service = ServiceSource(openRemoteConnection())
    private val updater = RuleDataUpdater(service, local, remote)

    fun isValidService(): Boolean = ExceptionUtils.fallback({ service.connection.version == Constants.REMOTE_SERVICE_VERSION }, false)
    fun queryAllMetadata(): PackagesMetadata = local.queryAllPackages() ?: PackagesMetadata(emptyList())
    fun queryPackage(pkg: String): PackageRuleSet? = local.queryPackage(pkg)
    fun isPackageEnabled(pkg: String): Boolean = service.queryPackage(pkg) != null
    fun enablePackage(pkg: String) = local.queryPackage(pkg)?.let { service.savePackage(pkg, it) }
    fun disablePackage(pkg: String) = service.removePackage(pkg)

    interface RuleDataCallback {
        fun onStateChanged(state: RuleDataState, result: RuleDataStateResult)
    }
}
