package com.github.kr328.ibr.data.sources

import com.github.kr328.ibr.model.StoreRuleSet
import com.github.kr328.ibr.model.StoreRuleSets
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
    override fun queryAllPackages(): StoreRuleSets? {
        return try {
            baseDir.resolve("packages.json").takeIf(File::exists)?.let {
                Json(DEFAULT_JSON_CONFIGURE).parse(StoreRuleSets.serializer(), it.readText())
            }
        } catch (e: Exception) {
            null
        }
    }

    @Synchronized
    override fun queryPackage(pkg: String): StoreRuleSet? {
        return try {
            baseDir.resolve("rules/$pkg.json").takeIf(File::exists)?.let {
                Json(DEFAULT_JSON_CONFIGURE).parse(StoreRuleSet.serializer(), it.readText())
            }
        } catch (e: Exception) {
            null
        }
    }

    @Synchronized
    override fun saveAllPackages(data: StoreRuleSets) {
        try {
            baseDir.apply { mkdirs() }.resolve("packages.json")
                    .writeText(Json(DEFAULT_JSON_CONFIGURE).stringify(StoreRuleSets.serializer(), data))
        } catch (e: Exception) {
            throw BaseSource.SourceException("saveAllPackages", e)
        }
    }

    @Synchronized
    override fun savePackage(pkg: String, data: StoreRuleSet) {
        try {
            baseDir.resolve("rules").apply { mkdirs() }.resolve("$pkg.json")
                    .writeText(Json(DEFAULT_JSON_CONFIGURE).stringify(StoreRuleSet.serializer(), data))
        } catch (e: Exception) {
            throw BaseSource.SourceException("savePackage $pkg", e)
        }
    }

    @Synchronized
    override fun removePackage(pkg: String) {
        baseDir.resolve("rules").resolve("$pkg.json").delete()
    }
}