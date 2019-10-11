package com.github.kr328.ibr.data

import android.content.Context
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.model.StoreRuleSet
import com.github.kr328.ibr.model.StoreRuleSets
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File

class LocalRules(context: Context) {
    private val userData: File = context.dataDir.resolve(Constants.LOCAL_RULE_PATH).apply { mkdirs() }

    @Synchronized
    fun queryRuleSets(): StoreRuleSets {
        val file = userData.resolve("packages.json")

        if (!file.isFile)
            return StoreRuleSets(emptyList())

        return Json(JsonConfiguration.Stable.copy(strictMode = false))
                .parse(StoreRuleSets.serializer(), file.readText())
    }

    @Synchronized
    fun queryRuleSet(packageName: String): StoreRuleSet {
        val file = userData.resolve("rules/$packageName.json")

        if (!file.isFile)
            return StoreRuleSet("", "", emptyList())

        return Json(JsonConfiguration.Stable.copy(strictMode = false))
                .parse(StoreRuleSet.serializer(), file.readText())
    }

    @Synchronized
    fun saveRuleSets(ruleSets: StoreRuleSets) {
        val file = userData.resolve("packages.json")

        file.writeText(Json(JsonConfiguration.Stable).stringify(StoreRuleSets.serializer(), ruleSets))
    }

    @Synchronized
    fun saveRuleSet(packageName: String, ruleSet: StoreRuleSet) {
        val file = userData.resolve("rules/$packageName.json")

        file.writeText(Json(JsonConfiguration.Stable).stringify(StoreRuleSet.serializer(), ruleSet))
    }
}