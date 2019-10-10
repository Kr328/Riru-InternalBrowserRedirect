package com.github.kr328.ibr.data.sources

import com.github.kr328.ibr.model.OnlineRuleSets
import com.github.kr328.ibr.model.OnlineRuleSet
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File

class LocalRepoSource(private val baseDir: File) : BaseSource {
    companion object {
        private val DEFAULT_JSON_CONFIGURE = JsonConfiguration.Stable.copy(strictMode = false)
    }

    fun getLastUpdate(): Long = baseDir.resolve("packages.json").takeIf(File::exists)?.lastModified()
            ?: -1

    @Synchronized
    override fun queryAllPackages(): OnlineRuleSets? {
        return try {
            baseDir.resolve("packages.json").takeIf(File::exists)?.let {
                Json(DEFAULT_JSON_CONFIGURE).parse(OnlineRuleSets.serializer(), it.readText())
            }
        } catch (e: Exception) {
            null
        }
    }

    @Synchronized
    override fun queryPackage(pkg: String): OnlineRuleSet? {
        return try {
            baseDir.resolve("rules/$pkg.json").takeIf(File::exists)?.let {
                Json(DEFAULT_JSON_CONFIGURE).parse(OnlineRuleSet.serializer(), it.readText())
            }
        } catch (e: Exception) {
            null
        }
    }

    @Synchronized
    override fun saveAllPackages(data: OnlineRuleSets) {
        try {
            baseDir.apply { mkdirs() }.resolve("packages.json")
                    .writeText(Json(DEFAULT_JSON_CONFIGURE).stringify(OnlineRuleSets.serializer(), data))
        } catch (e: Exception) {
            throw BaseSource.SourceException("saveAllPackages", e)
        }
    }

    @Synchronized
    override fun savePackage(pkg: String, data: OnlineRuleSet) {
        try {
            baseDir.resolve("rules").apply { mkdirs() }.resolve("$pkg.json")
                    .writeText(Json(DEFAULT_JSON_CONFIGURE).stringify(OnlineRuleSet.serializer(), data))
        } catch (e: Exception) {
            throw BaseSource.SourceException("savePackage $pkg", e)
        }
    }

    @Synchronized
    override fun removePackage(pkg: String) {
        baseDir.resolve("rules").resolve("$pkg.json").delete()
    }
}