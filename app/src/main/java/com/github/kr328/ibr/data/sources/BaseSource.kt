package com.github.kr328.ibr.data.sources

import com.github.kr328.ibr.model.StoreRuleSet
import com.github.kr328.ibr.model.StoreRuleSets

interface BaseSource {
    fun queryAllPackages(): StoreRuleSets?
    fun queryPackage(pkg: String): StoreRuleSet?
    fun saveAllPackages(data: StoreRuleSets)
    fun savePackage(pkg: String, data: StoreRuleSet)
    fun removePackage(pkg: String)

    class SourceException(message: String, cause: Exception) : Exception(message, cause)
}