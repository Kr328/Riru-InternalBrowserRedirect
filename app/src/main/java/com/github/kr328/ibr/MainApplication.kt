package com.github.kr328.ibr

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.github.kr328.ibr.data.OnlineRuleRemote
import com.github.kr328.ibr.data.RuleDatabase
import com.github.kr328.ibr.remote.LiveRemoteService

class MainApplication : Application() {
    val remoteService by lazy { LiveRemoteService() }
    val onlineRuleRepo by lazy { OnlineRuleRemote(this) }
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
