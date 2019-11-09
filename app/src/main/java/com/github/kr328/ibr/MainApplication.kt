package com.github.kr328.ibr

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.github.kr328.ibr.data.LocalRules
import com.github.kr328.ibr.data.OnlineRuleRemote
import com.github.kr328.ibr.data.RuleDatabase
import com.github.kr328.ibr.middleware.AppListManager
import com.github.kr328.ibr.middleware.EditAppManager
import com.github.kr328.ibr.middleware.RemoteManager
import com.github.kr328.ibr.reducer.AppReducer
import org.rekotlin.Store
import java.util.concurrent.Executors

class MainApplication : Application() {
    val onlineRuleRemote by lazy { OnlineRuleRemote(this) }
    private val localRules by lazy { LocalRules(this) }
    val store by lazy {
        Store(
                reducer = AppReducer::handle,
                state = null,
                middleware = listOf(
                        AppListManager(this, localRules, onlineRuleRemote).handler,
                        EditAppManager(this, localRules, onlineRuleRemote).handler,
                        RemoteManager(localRules, onlineRuleRemote).handler
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
    val database by lazy {
        Room.databaseBuilder(
                this,
                RuleDatabase::class.java, "rule"
        ).build()
    }

    companion object {
        fun fromContext(context: Context): MainApplication = context.applicationContext as MainApplication
    }
}
