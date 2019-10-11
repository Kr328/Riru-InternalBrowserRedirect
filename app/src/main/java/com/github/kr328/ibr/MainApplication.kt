package com.github.kr328.ibr

import android.app.Application
import android.content.Context
import com.github.kr328.ibr.middleware.AppListManager
import com.github.kr328.ibr.reducer.AppReducer
import org.rekotlin.Store

class MainApplication : Application() {
    val store by lazy {
        Store(
                reducer = AppReducer::handle,
                state = null,
                middleware = listOf(
                        AppListManager(this).handler
                )
        )
    }

    companion object {
        fun fromContext(context: Context): MainApplication = context.applicationContext as MainApplication
    }
}


