package com.github.kr328.ibr.data

import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.data.sources.BaseSource
import com.github.kr328.ibr.data.sources.LocalRepoSource
import com.github.kr328.ibr.data.sources.RemoteRepoSource
import com.github.kr328.ibr.data.sources.ServiceSource
import com.github.kr328.ibr.data.state.RuleDataState
import com.github.kr328.ibr.data.state.RuleDataStateResult
import com.github.kr328.ibr.model.PackagesMetadata
import com.github.kr328.ibr.utils.ExceptionUtils
import com.github.kr328.ibr.utils.SingleThreadPool
import java.util.*
import java.util.concurrent.PriorityBlockingQueue

class RuleDataUpdater(private val service: ServiceSource,
                      private val local: LocalRepoSource,
                      private val remote: RemoteRepoSource) {
    private val callbacks = Collections.synchronizedList<RuleData.RuleDataCallback>(mutableListOf())
    private val pool = SingleThreadPool()
    private val priorityUpdate = Collections.synchronizedList(mutableListOf<String>())

    fun getCurrentState(): RuleDataState = if ( pool.isRunning() ) RuleDataState.UPDATE_PACKAGES else RuleDataState.IDLE

    fun refresh(force: Boolean) {
        pool.execute {
            try {
                if ( !force ) {
                    if ( System.currentTimeMillis() - local.getLastUpdate() < Constants.DEFAULT_RULE_INVALIDATE )
                        return@execute
                }

                callbacks.forEach { it.onStateChanged(RuleDataState.UPDATE_PACKAGES) }

                val servicePackages = service.queryAllPackages()?.packages?.map(PackagesMetadata.Package::pkg)?.toSet() ?: emptySet()

                val remotePackages = remote.queryAllPackages()
                val remotePackagesMap = remotePackages.packages.map { it.pkg to it }.toMap()

                val requireUpdate = ((local.queryAllPackages()?.packages?.filter {
                    remotePackagesMap[it.pkg]?.version != it.version
                } ?: remotePackages.packages).map(PackagesMetadata.Package::pkg) ).toMutableSet()

                callbacks.forEach { it.onStateResult(RuleDataStateResult(RuleDataState.UPDATE_PACKAGES, true, emptyMap())) }

                callbacks.forEach { it.onStateChanged(RuleDataState.UPDATE_SINGLE_PACKAGE) }

                while ( requireUpdate.isNotEmpty() ) {
                    val pkg = if ( priorityUpdate.isNotEmpty() )
                        priorityUpdate.removeAt(0)
                    else
                        requireUpdate.first()

                    if ( requireUpdate.remove(pkg) ) {
                        try {
                            val data = remote.queryPackage(pkg)

                            local.savePackage(pkg, data)

                            if ( servicePackages.contains(pkg) )
                                service.savePackage(pkg, data)

                            callbacks.forEach { it.onStateResult(RuleDataStateResult(RuleDataState.UPDATE_SINGLE_PACKAGE, true, pkg)) }
                        }
                        catch (e: BaseSource.SourceException) {
                            callbacks.forEach { it.onStateResult(RuleDataStateResult(RuleDataState.UPDATE_SINGLE_PACKAGE, false, pkg)) }
                        }
                    }
                }

                priorityUpdate.clear()
            }
            catch (e: BaseSource.SourceException) {
                callbacks.forEach { it.onStateResult(RuleDataStateResult(RuleDataState.UPDATE_PACKAGES, false, emptyMap())) }
            }

            callbacks.forEach { it.onStateChanged(RuleDataState.IDLE) }
        }
    }

    fun requestPriority(pkg: String) {
        if ( !pool.isRunning() )
            return

        priorityUpdate.add(pkg)
    }

    fun registerCallback(callback: RuleData.RuleDataCallback) = callbacks.add(callback)
    fun unregisterCallback(callback: RuleData.RuleDataCallback) = callbacks.remove(callback)
}