package com.github.kr328.ibr.data.sources

import com.github.kr328.ibr.model.Packages
import com.github.kr328.ibr.remote.IRemoteService
import com.github.kr328.ibr.remote.model.Rule
import com.github.kr328.ibr.remote.model.RuleSet
import com.github.kr328.ibr.remote.openRemoteConnection

class ServiceSource : BaseSource {
    val connection: IRemoteService by lazy {
        openRemoteConnection()
    }

    override fun queryAllPackages(): Packages? {
        val packages = connection.queryAllRuleSet()
                .mapKeys { it.key as String }
                .mapValues { it.value as RuleSet }

        return Packages(packages.map {
            Packages.Package(it.key, -1)
        })
    }

    override fun queryPackage(pkg: String): com.github.kr328.ibr.model.RuleSet? {
        val packageData = connection.queryRuleSet(pkg)

        return packageData?.let {
            com.github.kr328.ibr.model.RuleSet(it.tag, "", it.token.toIntOrNull() ?: -1, it.rules.map { r ->
                com.github.kr328.ibr.model.RuleSet.Rule(r.tag, r.urlPath,
                        com.github.kr328.ibr.model.RuleSet.UrlFilters(r.regexIgnore.toRegex(), r.regexForce.toRegex()))
            })
        }
    }

    override fun saveAllPackages(data: Packages) = throw UnsupportedOperationException("Unsupported")

    override fun savePackage(pkg: String, data: com.github.kr328.ibr.model.RuleSet) {
        val ruleSet = RuleSet()

        ruleSet.tag = data.tag
        ruleSet.token = data.version.toString()
        ruleSet.rules = data.rules.map {
            Rule().apply {
                tag = it.tag
                urlPath = it.urlSource
                regexIgnore = it.urlFilters.ignore.toPattern()
                regexForce = it.urlFilters.force.toPattern()
            }
        }

        connection.updateRuleSet(pkg, ruleSet)
    }

    override fun removePackage(pkg: String) {
        connection.removeRuleSet(pkg)
    }

    fun updateSetting(feature: String, enabled: Boolean) {
        connection.updateSetting(feature, enabled)
    }
}