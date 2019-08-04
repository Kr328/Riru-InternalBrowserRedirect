package com.github.kr328.ibr.model

import kotlinx.serialization.Serializable

@Serializable
data class PackagesMetadata(val packages: List<Package>) {
    @Serializable
    data class Package(val pkg: String, val version: Int)
}