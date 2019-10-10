package com.github.kr328.ibr.data.sources

import com.github.kr328.ibr.model.OnlineRuleSets
import com.github.kr328.ibr.model.OnlineRuleSet
import com.github.kr328.ibr.utils.SimpleHttpClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.net.URL

class RemoteRepoSource(private val repo: RemoteRepo) : BaseSource {
    companion object {
        private val DEFAULT_JSON_CONFIGURE = JsonConfiguration.Stable.copy(strictMode = false)
    }

    interface RemoteRepo {
        fun getUser(): String
        fun getRepo(): String
        fun getBranch(): String
    }

    override fun queryAllPackages(): OnlineRuleSets {
        try {
            return Json(DEFAULT_JSON_CONFIGURE).parse(OnlineRuleSets.serializer(),
                    SimpleHttpClient.get(URL("https://raw.githubusercontent.com/${repo.getUser()}/${repo.getRepo()}/${repo.getBranch()}/packages.json")))
        } catch (e: Exception) {
            throw BaseSource.SourceException("RemoteRepoSource.queryAllPackages", e)
        }
    }

    override fun queryPackage(pkg: String): OnlineRuleSet {
        try {
            return Json(DEFAULT_JSON_CONFIGURE).parse(OnlineRuleSet.serializer(),
                    SimpleHttpClient.get(URL("https://raw.githubusercontent.com/${repo.getUser()}/${repo.getRepo()}/${repo.getBranch()}/rules/$pkg.json")))
        } catch (e: Exception) {
            throw BaseSource.SourceException("RemoteRepoSource.queryPackage $pkg", e)
        }
    }

    override fun saveAllPackages(data: OnlineRuleSets) = throw UnsupportedOperationException("Unsupported")
    override fun savePackage(pkg: String, data: OnlineRuleSet) = throw UnsupportedOperationException("Unsupported")
    override fun removePackage(pkg: String) = throw UnsupportedOperationException("Unsupported")
}