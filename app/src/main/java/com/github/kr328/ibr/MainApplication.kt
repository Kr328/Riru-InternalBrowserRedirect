package com.github.kr328.ibr

import android.app.Application
import android.content.Context
import com.github.kr328.ibr.middleware.AppListManager
import com.github.kr328.ibr.middleware.EditAppManager
import com.github.kr328.ibr.reducer.AppReducer
import org.rekotlin.StateType
import org.rekotlin.Store
import org.rekotlin.StoreType
import java.util.concurrent.Executors

class MainApplication : Application() {
    val store by lazy {
        Store(
                reducer = AppReducer::handle,
                state = null,
                middleware = listOf(
                        AppListManager(this).handler,
                        EditAppManager(this).handler
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


