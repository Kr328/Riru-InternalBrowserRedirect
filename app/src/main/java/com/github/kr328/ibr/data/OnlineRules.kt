package com.github.kr328.ibr.data.sources

import android.content.Context
import com.github.kr328.ibr.BuildConfig
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.SettingsActivity
import com.github.kr328.ibr.model.RuleSet
import com.github.kr328.ibr.model.RuleSets
import com.github.kr328.ibr.utils.SimpleCachedHttpClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class OnlineRules(context: Context) {
    private val preference = context.getSharedPreferences(BuildConfig.APPLICATION_ID + ".general", Context.MODE_PRIVATE)
    private val httpClient = SimpleCachedHttpClient(context.cacheDir.resolve("online-rules"), buildBaseUrl())

    init {
        preference.registerOnSharedPreferenceChangeListener {_, _ ->
            httpClient.baseUrl = buildBaseUrl()
        }
    }

    private fun buildBaseUrl(): String {
        val user = preference.getString(SettingsActivity.SETTING_ONLINE_RULE_USER_KEY, Constants.DEFAULT_RULE_GITHUB_USER)
        val repo = preference.getString(SettingsActivity.SETTING_ONLINE_RULE_REPO_KEY, Constants.DEFAULT_RULE_REPO)
        val branch = preference.getString(SettingsActivity.SETTING_ONLINE_RULE_BRANCH_KEY, Constants.DEFAULT_RULE_BRANCH)

        return "https://raw.githubusercontent.com/$user/$repo/$branch/packages.json"
    }

    fun queryRuleSets(cacheFirst: Boolean, ignoreCache: Boolean): RuleSets =
            Json(JsonConfiguration.Stable.copy(strictMode = false))
                    .parse(RuleSets.serializer(), httpClient.get("packages.json", cacheFirst, ignoreCache))
    fun queryRuleSet(packageName: String, cacheFirst: Boolean, ignoreCache: Boolean): RuleSet =
            Json(JsonConfiguration.Stable.copy(strictMode = false))
                    .parse(RuleSet.serializer(), httpClient.get("rules/$packageName.json", cacheFirst, ignoreCache))
}