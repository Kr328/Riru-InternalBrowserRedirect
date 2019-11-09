package com.github.kr328.ibr.utils

import kotlinx.io.IOException
import java.net.URL

class SimpleRelativeHttpClient(var baseUrl: String) {
    @Synchronized
    fun get(file: String) = SimpleHttpClient.get(URL("$baseUrl/$file"))

    fun getOrNull(file: String): String? {
        return try {
            get(file)
        } catch (e: IOException) {
            null
        }
    }
}