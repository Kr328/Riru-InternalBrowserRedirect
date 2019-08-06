package com.github.kr328.ibr

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.github.kr328.ibr.data.RuleData
import com.github.kr328.ibr.data.sources.RemoteRepoSource

class MainApplication : Application() {
    lateinit var ruleData: RuleData
    lateinit var general: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        general = getSharedPreferences(BuildConfig.APPLICATION_ID + ".general", Context.MODE_PRIVATE);
        ruleData = RuleData(this.cacheDir.resolve(Constants.CACHE_ONLINE_RULE_PATH), object : RemoteRepoSource.RemoteRepo {
            override fun getUser(): String = general.getString(SettingsActivity.SETTING_ONLINE_RULE_USER_KEY, Constants.DEFAULT_RULE_GITHUB_USER)!!
            override fun getRepo(): String = general.getString(SettingsActivity.SETTING_ONLINE_RULE_REPO_KEY, Constants.DEFAULT_RULE_REPO)!!
            override fun getBranch(): String = general.getString(SettingsActivity.SETTING_ONLINE_RULE_BRANCH_KEY, Constants.DEFAULT_RULE_BRANCH)!!
        })
    }

    companion object {
        fun fromContext(context: Context): MainApplication = context.applicationContext as MainApplication
    }
}


