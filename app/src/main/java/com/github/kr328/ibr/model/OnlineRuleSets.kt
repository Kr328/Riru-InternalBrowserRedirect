package com.github.kr328.ibr.model

import kotlinx.serialization.Serializable

@Serializable
data class OnlineRuleSets(val packages: List<Data>) {
    @Serializable
    data class Data(val packageName: String)
}