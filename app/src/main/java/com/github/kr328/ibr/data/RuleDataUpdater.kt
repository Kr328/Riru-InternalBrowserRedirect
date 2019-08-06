package com.github.kr328.ibr.data

import android.util.Log
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.data.sources.BaseSource
import com.github.kr328.ibr.data.sources.LocalRepoSource
import com.github.kr328.ibr.data.sources.RemoteRepoSource
import com.github.kr328.ibr.data.sources.ServiceSource
import com.github.kr328.ibr.data.state.RuleDataState
import com.github.kr328.ibr.data.state.RuleDataStateResult
import com.github.kr328.ibr.model.Packages
import com.github.kr328.ibr.utils.SingleThreadPool
import java.util.*

class RuleDataUpdater(private val service: ServiceSource,
                      private val local: LocalRepoSource,
                      private val remote: RemoteRepoSource) {
    private val callbacks = Collections.synchronizedList<RuleData.RuleDataCallback>(mutableListOf())
    private val pool = SingleThreadPool()
    private val priorityUpdate = Collections.synchronizedList(mutableListOf<String>())

    var currentState = RuleDataState.IDLE
        private set(value) {
            field = value
            callbacks.forEach { it.onStateChanged(value) }
        }

    fun refresh(force: Boolean) {
        pool.execute {
            try {
                currentState = RuleDataState.UPDATE_PACKAGES

                if (!force) {
                    if (System.currentTimeMillis() - local.getLastUpdate() < Constants.DEFAULT_RULE_INVALIDATE) {
                        callbacks.forEach { it.onStateResult(RuleDataStateResult(RuleDataState.UPDATE_PACKAGES, true, emptyMap())) }
                        currentState = RuleDataState.IDLE
                        return@execute
                    }
                }

                val servicePackages = service.queryAllPackages()?.packages?.map(Packages.Package::packageName)?.toSet()
                        ?: emptySet()

                val remotePackages = remote.queryAllPackages()
                val remotePackagesMap = remotePackages.packages.map { it.packageName to it }.toMap()

                val requireUpdate = ((local.queryAllPackages()?.packages?.filter {
                    remotePackagesMap[it.packageName]?.version != it.version
                } ?: remotePackages.packages).map(Packages.Package::packageName)).toMutableSet()

                currentState = RuleDataState.UPDATE_SINGLE_PACKAGE

                while (requireUpdate.isNotEmpty()) {
                    val pkg = if (priorityUpdate.isNotEmpty())
                        priorityUpdate.removeAt(0)
                    else
                        requireUpdate.first()

                    if (requireUpdate.remove(pkg)) {
                        val data = remote.queryPackage(pkg)

                        local.savePackage(pkg, data)

                        if (servicePackages.contains(pkg))
                            service.savePackage(pkg, data)

                        callbacks.forEach { it.onStateResult(RuleDataStateResult(RuleDataState.UPDATE_SINGLE_PACKAGE, true, pkg)) }
                    }
                }

                local.saveAllPackages(remotePackages)

                priorityUpdate.clear()

                callbacks.forEach { it.onStateResult(RuleDataStateResult(RuleDataState.UPDATE_PACKAGES, true, emptyMap())) }
            } catch (e: BaseSource.SourceException) {
                Log.e(Constants.TAG, "Updating Remote Rules", e)
                callbacks.forEach { it.onStateResult(RuleDataStateResult(RuleDataState.UPDATE_PACKAGES, false, emptyMap())) }
            }

            currentState = RuleDataState.IDLE
        }
    }

    fun requestPriority(pkg: String) {
        if (!pool.isRunning())
            return

        priorityUpdate.add(pkg)
    }

    fun registerCallback(callback: RuleData.RuleDataCallback) = callbacks.add(callback)
    fun unregisterCallback(callback: RuleData.RuleDataCallback) = callbacks.remove(callback)
}