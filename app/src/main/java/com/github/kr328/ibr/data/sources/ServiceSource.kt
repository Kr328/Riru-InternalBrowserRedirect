package com.github.kr328.ibr.data.sources

import com.github.kr328.ibr.model.PackageRuleSet
import com.github.kr328.ibr.model.PackagesMetadata
import com.github.kr328.ibr.remote.IRemoteService
import com.github.kr328.ibr.remote.model.Rule
import com.github.kr328.ibr.remote.model.RuleSet

class ServiceSource(val connection: IRemoteService) : BaseSource {
    override fun queryAllPackages(): PackagesMetadata? {
        val packages = connection.queryAllRuleSet()
                .mapKeys { it.key as String }
                .mapValues { it.value as RuleSet }

        return PackagesMetadata(packages.map {
            PackagesMetadata.Package(it.key, -1)
        })
    }

    override fun queryPackage(pkg: String): PackageRuleSet? {
        val packageData = connection.queryRuleSet(pkg)

        return packageData?.let {
            PackageRuleSet(it.tag, "", it.token.toIntOrNull() ?: -1, it.rules.map { r ->
                PackageRuleSet.Rule(r.tag, r.urlPath,
                        PackageRuleSet.UrlFilters(r.regexIgnore.toRegex(), r.regexForce.toRegex()))
            })
        }
    }

    override fun saveAllPackages(data: PackagesMetadata) = throw UnsupportedOperationException("Unsupported")

    override fun savePackage(pkg: String, data: PackageRuleSet) {
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
}