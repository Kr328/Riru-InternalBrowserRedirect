package com.github.kr328.ibr.data

import android.content.Context
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.model.OnlineRuleSet
import com.github.kr328.ibr.model.OnlineRuleSets
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File

class LocalRules(context: Context) {
    private val userData: File = context.dataDir.resolve(Constants.LOCAL_RULE_PATH).apply { mkdirs() }

    @Synchronized
    fun queryRuleSets(): OnlineRuleSets {
        val file = userData.resolve("packages.json")

        if ( !file.isFile )
            return OnlineRuleSets(emptyList())

        return Json(JsonConfiguration.Stable.copy(strictMode = false))
                .parse(OnlineRuleSets.serializer(), file.readText())
    }

    @Synchronized
    fun queryRuleSet(packageName: String): OnlineRuleSet {
        val file = userData.resolve("rules/$packageName.json")

        if ( !file.isFile )
            return OnlineRuleSet("", "", emptyList())

        return Json(JsonConfiguration.Stable.copy(strictMode = false))
                .parse(OnlineRuleSet.serializer(), file.readText())
    }

    @Synchronized
    fun saveRuleSets(ruleSets: OnlineRuleSets) {
        val file = userData.resolve("packages.json")

        file.writeText(Json(JsonConfiguration.Stable).stringify(OnlineRuleSets.serializer(), ruleSets))
    }

    @Synchronized
    fun saveRuleSet(packageName: String, ruleSet: OnlineRuleSet) {
        val file = userData.resolve("rules/$packageName.json")

        file.writeText(Json(JsonConfiguration.Stable).stringify(OnlineRuleSet.serializer(), ruleSet))
    }
}