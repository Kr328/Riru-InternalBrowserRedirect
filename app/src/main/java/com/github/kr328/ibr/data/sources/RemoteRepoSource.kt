package com.github.kr328.ibr.data.sources

import com.github.kr328.ibr.model.Packages
import com.github.kr328.ibr.model.RuleSet
import com.github.kr328.ibr.utils.SimpleHttpClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.net.URL

class RemoteRepoSource(private val user: String, private val repo: String) : BaseSource {
    override fun queryAllPackages(): Packages {
        try {
            return Json(JsonConfiguration.Stable.copy(strictMode = false)).parse(Packages.serializer(),
                    SimpleHttpClient.get(URL("https://raw.githubusercontent.com/$user/$repo/master/packages.json")))
        } catch (e: Exception) {
            throw BaseSource.SourceException("queryAllPackages", e)
        }
    }

    override fun queryPackage(pkg: String): RuleSet {
        try {
            return Json(JsonConfiguration.Stable.copy(strictMode = false)).parse(RuleSet.serializer(),
                    SimpleHttpClient.get(URL("https://raw.githubusercontent.com/$user/$repo/master/rules/$pkg.json")))
        } catch (e: Exception) {
            throw BaseSource.SourceException("queryPackage $pkg", e)
        }
    }

    override fun saveAllPackages(data: Packages) = throw UnsupportedOperationException("Unsupported")
    override fun savePackage(pkg: String, data: RuleSet) = throw UnsupportedOperationException("Unsupported")
    override fun removePackage(pkg: String) = throw UnsupportedOperationException("Unsupported")
}