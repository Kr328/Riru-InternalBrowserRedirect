package com.github.kr328.ibr.middleware

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import com.github.kr328.ibr.R
import com.github.kr328.ibr.action.EditAppCreatedActivityAction
import com.github.kr328.ibr.action.EditAppSetAppInfoAction
import com.github.kr328.ibr.state.AppState
import org.rekotlin.Middleware
import kotlin.concurrent.thread

class EditAppManager(context: Context) {
    val handler: Middleware<AppState> = { dispatch, _ ->
        { next ->
            { action ->
                when ( action ) {
                    is EditAppCreatedActivityAction -> {
                        thread {
                            try {
                                val pm = context.packageManager
                                val pkg = pm.getPackageInfo(action.packageName.toString(), 0)

                                dispatch(EditAppSetAppInfoAction(pkg.packageName,
                                        pkg.applicationInfo.loadLabel(pm),
                                        pkg.versionName,
                                        pkg.applicationInfo.loadIcon(pm)))
                            }
                            catch (e: PackageManager.NameNotFoundException) {
                                dispatch(EditAppSetAppInfoAction(action.packageName,
                                        context.getString(R.string.edit_app_application_rule_set_unknown),
                                        context.getString(R.string.edit_app_application_rule_set_unknown),
                                        context.getDrawable(android.R.drawable.sym_def_app_icon) ?: ColorDrawable()))
                            }
                        }
                    }
                }
                next(action)
            }
        }
    }
}