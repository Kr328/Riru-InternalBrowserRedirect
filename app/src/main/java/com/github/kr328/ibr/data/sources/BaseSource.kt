package com.github.kr328.ibr.data.sources

import com.github.kr328.ibr.model.OnlineRuleSet
import com.github.kr328.ibr.model.OnlineRuleSets

interface BaseSource {
    fun queryAllPackages(): OnlineRuleSets?
    fun queryPackage(pkg: String): OnlineRuleSet?
    fun saveAllPackages(data: OnlineRuleSets)
    fun savePackage(pkg: String, data: OnlineRuleSet)
    fun removePackage(pkg: String)

    class SourceException(message: String, cause: Exception) : Exception(message, cause)
}