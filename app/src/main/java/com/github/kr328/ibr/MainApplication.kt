package com.github.kr328.ibr

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.github.kr328.ibr.data.RuleData
import com.github.kr328.ibr.data.sources.RemoteRepoSource
import com.github.kr328.ibr.reducer.AppReducer
import com.github.kr328.ibr.state.AppState
import org.rekotlin.Store

class MainApplication : Application() {
    val ruleData: RuleData by lazy {
        RuleData(this, this.cacheDir.resolve(Constants.CACHE_ONLINE_RULE_PATH), object : RemoteRepoSource.RemoteRepo {
            override fun getUser(): String = general.getString(SettingsActivity.SETTING_ONLINE_RULE_USER_KEY, Constants.DEFAULT_RULE_GITHUB_USER)!!
            override fun getRepo(): String = general.getString(SettingsActivity.SETTING_ONLINE_RULE_REPO_KEY, Constants.DEFAULT_RULE_REPO)!!
            override fun getBranch(): String = general.getString(SettingsActivity.SETTING_ONLINE_RULE_BRANCH_KEY, Constants.DEFAULT_RULE_BRANCH)!!
        })
    }
    val general: SharedPreferences by lazy {
        getSharedPreferences(BuildConfig.APPLICATION_ID + ".general", Context.MODE_PRIVATE);
    }
    val store = Store(
            reducer = AppReducer::handle ,
            state = null
    )

    companion object {
        fun fromContext(context: Context): MainApplication = context.applicationContext as MainApplication
    }
}


