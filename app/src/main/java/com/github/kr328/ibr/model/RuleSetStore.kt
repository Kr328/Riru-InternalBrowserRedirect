package com.github.kr328.ibr.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RuleSetStore(val tag: String, val authors: String, val rules: List<Rule>) {
    @Serializable
    data class Rule(val tag: String, @SerialName("url-source") val urlSource: String, @SerialName("url-filter") val urlFilters: UrlFilters)

    @Serializable
    data class UrlFilters(val ignore: String, val force: String)
}
