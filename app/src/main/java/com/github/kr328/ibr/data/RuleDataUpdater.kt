package com.github.kr328.ibr.data

import android.content.Context
import com.github.kr328.ibr.data.sources.LocalRepoSource
import com.github.kr328.ibr.data.sources.RemoteRepoSource
import com.github.kr328.ibr.data.state.RuleDataState
import com.github.kr328.ibr.utils.SingleThreadPool
import java.util.*

class RuleDataUpdater(private val context: Context,
                      private val service: RemoteService,
                      private val local: LocalRepoSource,
                      private val remote: RemoteRepoSource) {
    private val callbacks = Collections.synchronizedList<RuleData.RuleDataCallback>(mutableListOf())
    private val pool = SingleThreadPool()

    var currentState = RuleDataState.IDLE
        private set(value) {
            field = value
            callbacks.forEach { it.onStateChanged(value) }
        }

    fun refresh(force: Boolean) {
//        pool.execute {
//            try {
//                if (!force && System.currentTimeMillis() - local.getLastUpdate() < Constants.DEFAULT_RULE_INVALIDATE)
//                    return@execute
//
//                currentState = RuleDataState.UPDATE_PACKAGES
//
//                val applications = context.packageManager.getInstalledApplications(0).map(ApplicationInfo::packageName)
//                val servicePackages = service.queryAllPackages()?.packages?.map(StoreRuleSets.Data::packageName)?.toSet()
//                        ?: emptySet()
//                val remotePackages = remote.queryAllPackages().packages.map { it.packageName to it }.toMap()
//                val localPackages = local.queryAllPackages()?.packages?.map { it.packageName to it }?.toMap()
//                        ?: emptyMap()
//
//                val localStore = remotePackages.keys.intersect(applications)
//
//                localStore.dropWhile {
//                    localPackages[it]?.version == remotePackages[it]?.version
//                }.forEach {
//                    with(remote.queryPackage(it)) {
//                        local.savePackage(it, this)
//                        if (servicePackages.contains(it))
//                            service.savePackage(it, this)
//                    }
//                }
//
//                (localPackages.keys - localStore).forEach(local::removePackage)
//
//                local.saveAllPackages(StoreRuleSets(remotePackages.filterKeys(localStore::contains).values.toList()))
//
//                callbacks.forEach { it.onStateResult(RuleDataStateResult(RuleDataState.UPDATE_PACKAGES, true)) }
//            } catch (e: BaseSource.SourceException) {
//                Log.e(Constants.TAG, "Updating Remote Rules", e)
//                callbacks.forEach { it.onStateResult(RuleDataStateResult(RuleDataState.UPDATE_PACKAGES, false)) }
//            }
//
//            currentState = RuleDataState.IDLE
//        }
    }

    fun registerCallback(callback: RuleData.RuleDataCallback) = callbacks.add(callback)
    fun unregisterCallback(callback: RuleData.RuleDataCallback) = callbacks.remove(callback)
}