package com.github.kr328.ibr.data

import com.github.kr328.ibr.data.sources.LocalRepoSource
import com.github.kr328.ibr.data.sources.RemoteRepoSource
import com.github.kr328.ibr.data.sources.ServiceSource
import java.util.*
import java.util.concurrent.PriorityBlockingQueue

class RuleDataUpdater(private val service: ServiceSource,
                      private val local: LocalRepoSource,
                      private val remote: RemoteRepoSource) {
    private val queue = PriorityBlockingQueue<Task>()
    private val callbacks = Collections.synchronizedList<RuleData.RuleDataCallback>(mutableListOf())
    private val thread = Thread {
        try {
            while (!Thread.interrupted())
                queue.poll()?.runnable?.invoke()
        }
        catch (e: InterruptedException) {}
    }

    init {
        thread.start()
    }

    protected fun finalize() {
        thread.interrupt()
    }

    private class Task(val priority: Int, val id: Long, val runnable: () -> Unit) : Comparable<Task> {
        override fun compareTo(other: Task): Int {
            return if ( id == other.id ) { 0 }
            else {
                if ( priority == other.priority )
                    (id - other.id).toInt()
                else
                    priority - other.priority
            }
        }
    }

    fun autoUpdate() {
        queue.add(Task(99, 0) {
            val remotePackages = remote.queryAllPackages()
            val remotePackagesMap = remotePackages.packages.map { it.pkg to it }.toMap()

            val requireUpdate = local.queryAllPackages()?.packages?.filter {
                remotePackagesMap[it.pkg]?.version != it.version
            }

            for ( r in requireUpdate ?: remotePackages.packages ) {
                queue.add(Task(99, r.pkg.hashCode().toLong()) {
                    local.savePackage(r.pkg, remote.queryPackage(r.pkg))
                })
            }

            queue.add(Task(99, Long.MAX_VALUE) {
                local.saveAllPackages(remotePackages)
            })
        })
    }
}