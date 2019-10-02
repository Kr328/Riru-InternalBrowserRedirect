package com.github.kr328.ibr.data

import android.content.Context
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.model.RuleSet
import com.github.kr328.ibr.model.RuleSets
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File

class LocalRules(context: Context) {
    private val userData: File = context.dataDir.resolve(Constants.LOCAL_RULE_PATH).apply { mkdirs() }

    @Synchronized
    fun queryRuleSets(): RuleSets {
        val file = userData.resolve("packages.json")

        if ( !file.isFile )
            return RuleSets(emptyList())

        return Json(JsonConfiguration.Stable.copy(strictMode = false))
                .parse(RuleSets.serializer(), file.readText())
    }

    @Synchronized
    fun queryRuleSet(packageName: String): RuleSet {
        val file = userData.resolve("rules/$packageName.json")

        if ( !file.isFile )
            return RuleSet("", "", emptyList())

        return Json(JsonConfiguration.Stable.copy(strictMode = false))
                .parse(RuleSet.serializer(), file.readText())
    }

    @Synchronized
    fun saveRuleSets(ruleSets: RuleSets) {
        val file = userData.resolve("packages.json")

        file.writeText(Json(JsonConfiguration.Stable).stringify(RuleSets.serializer(), ruleSets))
    }

    @Synchronized
    fun saveRuleSet(packageName: String, ruleSet: RuleSet) {
        val file = userData.resolve("rules/$packageName.json")

        file.writeText(Json(JsonConfiguration.Stable).stringify(RuleSet.serializer(), ruleSet))
    }
}