package com.github.kr328.ibr

import android.app.Application
import android.content.Context
import com.github.kr328.ibr.data.LocalRules
import com.github.kr328.ibr.data.OnlineRules
import com.github.kr328.ibr.middleware.AppListManager
import com.github.kr328.ibr.middleware.EditAppManager
import com.github.kr328.ibr.middleware.RemoteManager
import com.github.kr328.ibr.reducer.AppReducer
import org.rekotlin.Store
import java.util.concurrent.Executors

class MainApplication : Application() {
    private val onlineRules by lazy { OnlineRules(this) }
    private val localRules by lazy { LocalRules(this) }
    val store by lazy {
        Store(
                reducer = AppReducer::handle,
                state = null,
                middleware = listOf(
                        AppListManager(this, localRules, onlineRules).handler,
                        EditAppManager(this, localRules, onlineRules).handler,
                        RemoteManager(localRules, onlineRules).handler
                )
        ).apply {
            val original = dispatchFunction
            val executor = Executors.newSingleThreadExecutor()

            dispatchFunction = {
                executor.submit {
                    original(it)
                }
            }
        }
    }

    companion object {
        fun fromContext(context: Context): MainApplication = context.applicationContext as MainApplication
    }
}


