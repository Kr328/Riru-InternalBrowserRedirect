package com.github.kr328.ibr.middleware

import android.content.Context
import com.github.kr328.ibr.action.*
import com.github.kr328.ibr.data.LocalRules
import com.github.kr328.ibr.data.OnlineRules
import com.github.kr328.ibr.state.AppState
import com.github.kr328.ibr.tools.AppListLoader
import org.rekotlin.Middleware
import java.util.concurrent.Executors

class AppListManager(context: Context, localRules: LocalRules, onlineRules: OnlineRules) {
    private val loader = AppListLoader(localRules, onlineRules, context)
    private val executor = Executors.newSingleThreadExecutor()

    val handler: Middleware<AppState> = { dispatch, _ ->
        { next ->
            { action ->
                when (action) {
                    is AppListStartedAction -> {
                        executor.submit {
                            dispatch(AppListProgressAction(show = true))

                            val data = loader.load(cacheFirst = true, ignoreCache = false)

                            if (data != null)
                                dispatch(AppListUpdatedAction(data))
                            else
                                dispatch(AppListUpdateErrorAction(ALUError.LOAD_FAILURE))
                        }
                        executor.submit {
                            val data = loader.load(cacheFirst = false, ignoreCache = false)

                            if (data != null)
                                dispatch(AppListUpdatedAction(data))
                            else
                                dispatch(AppListUpdateErrorAction(ALUError.LOAD_FAILURE))

                            dispatch(AppListProgressAction(show = false))
                        }
                    }
                    is AppListRefreshAction -> {
                        executor.submit {
                            val data = loader.load(cacheFirst = false, ignoreCache = true)

                            if (data != null)
                                dispatch(AppListUpdatedAction(data))
                            else
                                dispatch(AppListUpdateErrorAction(ALUError.LOAD_FAILURE))

                            dispatch(AppListProgressAction(show = false))
                        }
                    }
                }
                next(action)
            }
        }
    }
}