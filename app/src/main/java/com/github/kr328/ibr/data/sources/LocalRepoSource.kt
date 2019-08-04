package com.github.kr328.ibr.data.sources

import com.github.kr328.ibr.model.PackageRuleSet
import com.github.kr328.ibr.model.PackagesMetadata
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File

class LocalRepoSource(private val baseDir: File) : BaseSource {
    @Synchronized
    override fun queryAllPackages(): PackagesMetadata? {
        try {
            return baseDir.resolve("packages.json").takeIf(File::exists)?.let {
                Json(JsonConfiguration.Stable).parse(PackagesMetadata.serializer(), it.readText())
            }
        }
        catch (e: Exception) {
            throw BaseSource.SourceException("queryAllPackages", e)
        }
    }

    @Synchronized
    override fun queryPackage(pkg: String): PackageRuleSet? {
        try {
            return baseDir.resolve("rules/$pkg.json").takeIf(File::exists)?.let {
                Json(JsonConfiguration.Stable).parse(PackageRuleSet.serializer(), it.readText())
            }
        }
        catch (e: Exception) {
            throw BaseSource.SourceException("queryPackage $pkg", e)
        }
    }

    @Synchronized
    override fun saveAllPackages(data: PackagesMetadata) {
        try {
            baseDir.also(File::mkdirs).resolve("packages.json")
                    .writeText(Json(JsonConfiguration.Stable).stringify(PackagesMetadata.serializer(), data))
        }
        catch (e: Exception) {
            throw BaseSource.SourceException("saveAllPackages", e)
        }
    }

    @Synchronized
    override fun savePackage(pkg: String, data: PackageRuleSet) {
        try {
            baseDir.resolve("rules").also(File::mkdirs).resolve("$pkg.json").let {
                it.writeText(Json(JsonConfiguration.Stable).stringify(PackageRuleSet.serializer(), data))
            }
        }
        catch (e: Exception) {
            throw BaseSource.SourceException("savePackage $pkg", e)
        }
    }

    @Synchronized
    override fun removePackage(pkg: String) {
        baseDir.resolve("rules").resolve("$pkg.json").delete()
    }
}