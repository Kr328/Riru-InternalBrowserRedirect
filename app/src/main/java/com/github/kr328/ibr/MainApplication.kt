package com.github.kr328.ibr

import android.app.Application
import android.content.Context
import com.github.kr328.ibr.data.RuleData

class MainApplication : Application() {
    lateinit var ruleData: RuleData

    override fun onCreate() {
        super.onCreate()

        ruleData = RuleData(this.cacheDir.resolve(Constants.CACHE_ONLINE_RULE_PATH), Constants.DEFAULT_RULE_GITHUB_USER, Constants.DEFAULT_RULE_REPO)
    }

    companion object {
        fun fromContext(context: Context): MainApplication = context.applicationContext as MainApplication
    }
}


