package com.github.kr328.ibr

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.github.kr328.ibr.data.OnlineRuleRemote
import com.github.kr328.ibr.data.RuleDatabase

class MainApplication : Application() {
    val onlineRuleRemote by lazy {
        OnlineRuleRemote(this)
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
