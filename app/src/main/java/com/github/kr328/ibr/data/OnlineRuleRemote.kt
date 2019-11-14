package com.github.kr328.ibr.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.github.kr328.ibr.BuildConfig
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.SettingsActivity
import com.github.kr328.ibr.model.RuleSetStore
import com.github.kr328.ibr.model.RuleSetsStore
import com.github.kr328.ibr.utils.SimpleRelativeHttpClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class OnlineRuleRemote(context: Context): SharedPreferences.OnSharedPreferenceChangeListener {
    private val preference = context.getSharedPreferences(BuildConfig.APPLICATION_ID + ".general", Context.MODE_PRIVATE)
    private val httpClient = SimpleRelativeHttpClient(buildBaseUrl())

    init {
        preference.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        httpClient.baseUrl = buildBaseUrl()
    }

    private fun buildBaseUrl(): String {
        val user = preference.getString(SettingsActivity.SETTING_ONLINE_RULE_USER_KEY, Constants.DEFAULT_RULE_GITHUB_USER)
        val repo = preference.getString(SettingsActivity.SETTING_ONLINE_RULE_REPO_KEY, Constants.DEFAULT_RULE_REPO)
        val branch = preference.getString(SettingsActivity.SETTING_ONLINE_RULE_BRANCH_KEY, Constants.DEFAULT_RULE_BRANCH)

        return "https://raw.githubusercontent.com/$user/$repo/$branch"
    }

    fun queryRuleSets(): RuleSetsStore =
            Json(JsonConfiguration.Stable.copy(strictMode = false))
                    .parse(RuleSetsStore.serializer(), httpClient.get("packages.json"))

    fun queryRuleSet(packageName: String): RuleSetStore =
            Json(JsonConfiguration.Stable.copy(strictMode = false))
                    .parse(RuleSetStore.serializer(), httpClient.get("rules/$packageName.json"))

    fun queryRuleSetOrNull(packageName: String): RuleSetStore? =
            httpClient.getOrNull("rules/$packageName.json")?.let {
                Json(JsonConfiguration.Stable.copy(strictMode = false))
                        .parse(RuleSetStore.serializer(), it)
            }
}