package com.github.kr328.ibr.model

import kotlinx.serialization.Serializable

@Serializable
data class Packages(val packages: List<Package>) {
    @Serializable
    data class Package(val packageName: String, val version: Int)
}