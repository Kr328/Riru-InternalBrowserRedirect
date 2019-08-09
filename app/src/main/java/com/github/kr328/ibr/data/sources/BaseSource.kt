package com.github.kr328.ibr.data.sources

import com.github.kr328.ibr.model.Packages
import com.github.kr328.ibr.model.RuleSet

interface BaseSource {
    fun queryAllPackages(): Packages?
    fun queryPackage(pkg: String): RuleSet?
    fun saveAllPackages(data: Packages)
    fun savePackage(pkg: String, data: RuleSet)
    fun removePackage(pkg: String)

    class SourceException(message: String, cause: Exception) : Exception(message, cause)
}