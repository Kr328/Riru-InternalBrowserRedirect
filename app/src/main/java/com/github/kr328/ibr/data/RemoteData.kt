package com.github.kr328.ibr.data

import android.os.RemoteException
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.remote.model.RuleSet
import com.github.kr328.ibr.remote.openRemoteConnection

object RemoteData {
    const val STATUS_CONNECT_REMOTE_FAILURE = 1
    const val STATUS_REMOTE_VERSION_INVALID = 2

    private val remoteConnection by lazy {
        openRemoteConnection().also {
            if ( it.version != Constants.REMOTE_SERVICE_VERSION )
                throw RemoteException("error_invalid_remote_version")
        }
    }

    fun queryAllRuleSets(): DataResult<Map<String, RuleSet>> {
        return try {
            val ruleSets = remoteConnection.queryAllRuleSet()
                    .mapKeys { it.key as String }
                    .mapValues { it.value as RuleSet }
            DataResult(DataResult.STATUS_SUCCESS, ruleSets)
        }
        catch (e: RemoteException) {
            when (e.message) {
                "error_invalid_remote_version" -> DataResult(STATUS_REMOTE_VERSION_INVALID, emptyMap())
                else -> DataResult(STATUS_CONNECT_REMOTE_FAILURE, emptyMap())
            }
        }
    }
}