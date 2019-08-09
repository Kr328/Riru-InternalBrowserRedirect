package com.github.kr328.ibr.data.sources

import com.github.kr328.ibr.model.Packages
import com.github.kr328.ibr.model.RuleSet
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
    override fun queryAllPackages(): Packages? {
        return try {
            baseDir.resolve("packages.json").takeIf(File::exists)?.let {
                Json(DEFAULT_JSON_CONFIGURE).parse(Packages.serializer(), it.readText())
            }
        } catch (e: Exception) {
            null
        }
    }

    @Synchronized
    override fun queryPackage(pkg: String): RuleSet? {
        return try {
            baseDir.resolve("rules/$pkg.json").takeIf(File::exists)?.let {
                Json(DEFAULT_JSON_CONFIGURE).parse(RuleSet.serializer(), it.readText())
            }
        } catch (e: Exception) {
            null
        }
    }

    @Synchronized
    override fun saveAllPackages(data: Packages) {
        try {
            baseDir.apply { mkdirs() }.resolve("packages.json")
                    .writeText(Json(DEFAULT_JSON_CONFIGURE).stringify(Packages.serializer(), data))
        } catch (e: Exception) {
            throw BaseSource.SourceException("saveAllPackages", e)
        }
    }

    @Synchronized
    override fun savePackage(pkg: String, data: RuleSet) {
        try {
            baseDir.resolve("rules").apply { mkdirs() }.resolve("$pkg.json").let {
                it.writeText(Json(DEFAULT_JSON_CONFIGURE).stringify(RuleSet.serializer(), data))
            }
        } catch (e: Exception) {
            throw BaseSource.SourceException("savePackage $pkg", e)
        }
    }

    @Synchronized
    override fun removePackage(pkg: String) {
        baseDir.resolve("rules").resolve("$pkg.json").delete()
    }
}