package com.github.kr328.ibr.middleware

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import com.github.kr328.ibr.R
import com.github.kr328.ibr.action.*
import com.github.kr328.ibr.data.LocalRules
import com.github.kr328.ibr.data.OnlineRules
import com.github.kr328.ibr.remote.RemoteConnection
import com.github.kr328.ibr.remote.shared.Rule
import com.github.kr328.ibr.state.AppState
import org.rekotlin.Middleware
import java.util.concurrent.Executors

class EditAppManager(context: Context, localRules: LocalRules, onlineRules: OnlineRules) {
    private val executor = Executors.newSingleThreadExecutor()

    val handler: Middleware<AppState> = { dispatch, _ ->
        { next ->
            { action ->
                when (action) {
                    is EditAppCreatedActivityAction -> {
                        executor.submit {
                            try {
                                val pm = context.packageManager
                                val pkg = pm.getPackageInfo(action.packageName, 0)

                                dispatch(EditAppSetAppInfoAction(pkg.packageName,
                                        pkg.applicationInfo.loadLabel(pm),
                                        pkg.versionName,
                                        pkg.applicationInfo.loadIcon(pm)))
                            } catch (e: PackageManager.NameNotFoundException) {
                                dispatch(EditAppSetAppInfoAction(action.packageName,
                                        context.getString(R.string.edit_app_application_rule_set_unknown),
                                        context.getString(R.string.edit_app_application_rule_set_unknown),
                                        context.getDrawable(android.R.drawable.sym_def_app_icon)
                                                ?: ColorDrawable()))
                            }
                        }
                    }
                    is EditAppStartedActivityAction -> {
                        executor.submit {
                            dispatch(EditAppSetRefreshingAction(true))

                            val ruleSet = RemoteConnection.connection.queryRuleSet(action.packageName)

                            val local = localRules.queryRuleSet(action.packageName)

                            try {
                                val online = onlineRules.queryRuleSet(action.packageName,
                                        cacheFirst = false, ignoreCache = false)

                                dispatch(EditAppSetRuleSetEnabledAction(ruleSet.extras.contains("local"), ruleSet.extras.contains("online")))
                                dispatch(EditAppSetRuleSetAction(online, local))
                            } catch (e: Exception) {
                                val online = runCatching { onlineRules.queryRuleSet(action.packageName,
                                        cacheFirst = true, ignoreCache = false) }.getOrNull()

                                dispatch(EditAppSetRuleSetEnabledAction(ruleSet.extras.contains("local"), ruleSet.extras.contains("online")))
                                dispatch(EditAppSetRuleSetAction(online, local))
                            }

                            dispatch(RemoteUpdateRuleSetAction(action.packageName))

                            dispatch(EditAppSetRefreshingAction(false))
                        }
                    }
                    is EditAppRefreshAction -> {
                        executor.submit {
                            dispatch(EditAppSetRefreshingAction(true))

                            val ruleSet = RemoteConnection.connection.queryRuleSet(action.packageName)

                            val local = localRules.queryRuleSet(action.packageName)

                            try {
                                val online = onlineRules.queryRuleSet(action.packageName,
                                        cacheFirst = false, ignoreCache = true)

                                dispatch(EditAppSetRuleSetEnabledAction(ruleSet.extras.contains("local"), ruleSet.extras.contains("online")))
                                dispatch(EditAppSetRuleSetAction(online, local))
                            } catch (e: Exception) {
                                val online = runCatching { onlineRules.queryRuleSet(action.packageName,
                                        cacheFirst = true, ignoreCache = false) }.getOrNull()

                                dispatch(EditAppSetRuleSetEnabledAction(ruleSet.extras.contains("local"), ruleSet.extras.contains("online")))
                                dispatch(EditAppSetRuleSetAction(online, local))
                            }

                            dispatch(RemoteUpdateRuleSetAction(action.packageName))

                            dispatch(EditAppSetRefreshingAction(false))
                        }
                    }
                    is EditAppUserSetEnabledAction -> {
                        dispatch(EditAppSetRuleSetEnabledAction(action.localEnable, action.onlineEnable))
                        dispatch(RemoteUpdateRuleSetAction(action.packageName))
                    }
                    else -> next(action)
                }
            }
        }
    }
}
