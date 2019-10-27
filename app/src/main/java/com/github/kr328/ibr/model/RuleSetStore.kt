@file:UseSerializers(RegexSerializer::class, UriSerializer::class)

package com.github.kr328.ibr.model

import android.net.Uri
import com.github.kr328.ibr.serializers.RegexSerializer
import com.github.kr328.ibr.serializers.UriSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class RuleSetStore(val tag: String, val authors: String, val rules: List<Rule>) {
    @Serializable
    data class Rule(val tag: String, @SerialName("url-source") val urlSource: Uri, @SerialName("url-filter") val urlFilters: UrlFilters)

    @Serializable
    data class UrlFilters(val ignore: Regex, val force: Regex)
}
