package com.github.kr328.ibr.data.sources

import com.github.kr328.ibr.model.PackageRuleSet
import com.github.kr328.ibr.model.PackagesMetadata

interface BaseSource {
    fun queryAllPackages(): PackagesMetadata?
    fun queryPackage(pkg: String): PackageRuleSet?
    fun saveAllPackages(data: PackagesMetadata)
    fun savePackage(pkg: String, data: PackageRuleSet)
    fun removePackage(pkg: String)

    class SourceException(message: String, cause: Exception) : Exception(message, cause)
}