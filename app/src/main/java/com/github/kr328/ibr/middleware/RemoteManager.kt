package com.github.kr328.ibr.middleware

import com.github.kr328.ibr.action.RemoteUpdateRuleSetAction
import com.github.kr328.ibr.data.LocalRules
import com.github.kr328.ibr.data.OnlineRules
import com.github.kr328.ibr.remote.RemoteConnection
import com.github.kr328.ibr.remote.shared.Rule
import com.github.kr328.ibr.state.AppState
import org.rekotlin.Middleware
import java.util.concurrent.Executors

class RemoteManager(localRules: LocalRules, onlineRules: OnlineRules) {
    private val executor = Executors.newSingleThreadExecutor()

    val handler: Middleware<AppState> = { _, getState ->
        { next ->
            { action ->
                when ( action ) {
                    is RemoteUpdateRuleSetAction -> {
                        executor.submit {
                            val state = getState()

                            if ( state?.editAppState?.localEnable != true && state?.editAppState?.onlineEnable != true )
                                RemoteConnection.connection.removeRuleSet(action.packgeName)
                            else {
                                val local = localRules.queryRuleSet(action.packgeName)
                                val online = onlineRules.queryRuleSetOrNull(action.packgeName, cacheFirst = true, ignoreCache = false)

                                val ruleSet = RemoteConnection.connection.queryRuleSet(action.packgeName)

                                ruleSet.rules = ((local?.rules?.takeIf { state.editAppState.localEnable } ?: emptyList())
                                        + (online?.rules?.takeIf { state.editAppState.onlineEnable } ?: emptyList())).map {
                                    Rule().apply {
                                        tag = it.tag
                                        urlPath = it.urlSource
                                        regexIgnore = it.urlFilters.ignore.pattern
                                        regexForce = it.urlFilters.force.pattern
                                    }
                                }

                                ruleSet.tag = online?.tag ?: "default"
                                ruleSet.debug = state.editAppState.debug
                                ruleSet.extras = mutableListOf()

                                if (state.editAppState.onlineEnable)
                                    ruleSet.extras.add("online")
                                if (state.editAppState.localEnable)
                                    ruleSet.extras.add("local")

                                RemoteConnection.connection.updateRuleSet(action.packgeName, ruleSet)
                            }
                        }
                    }
                    else -> next(action)
                }
            }
        }
    }

}