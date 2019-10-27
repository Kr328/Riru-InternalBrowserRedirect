package com.github.kr328.ibr.data

import android.content.Context
import com.github.kr328.ibr.Constants
import com.github.kr328.ibr.model.RuleSetStore
import com.github.kr328.ibr.model.RuleSetsStore
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File

class LocalRules(context: Context) {
    private val userData: File = context.dataDir.resolve(Constants.LOCAL_RULE_PATH).apply { mkdirs() }

    @Synchronized
    fun queryRuleSets(): RuleSetsStore {
        val file = userData.resolve("packages.json")

        if (!file.isFile)
            return RuleSetsStore(emptyList())

        return Json(JsonConfiguration.Stable.copy(strictMode = false))
                .parse(RuleSetsStore.serializer(), file.readText())
    }

    @Synchronized
    fun queryRuleSet(packageName: String): RuleSetStore? {
        val file = userData.resolve("rules/$packageName.json")

        if (!file.isFile)
            return null

        return Json(JsonConfiguration.Stable.copy(strictMode = false))
                .parse(RuleSetStore.serializer(), file.readText())
    }

    @Synchronized
    fun saveRuleSets(ruleSets: RuleSetsStore) {
        val file = userData.resolve("packages.json")

        file.writeText(Json(JsonConfiguration.Stable).stringify(RuleSetsStore.serializer(), ruleSets))
    }

    @Synchronized
    fun saveRuleSet(packageName: String, ruleSet: RuleSetStore) {
        val file = userData.resolve("rules/$packageName.json")

        file.writeText(Json(JsonConfiguration.Stable).stringify(RuleSetStore.serializer(), ruleSet))
    }
}