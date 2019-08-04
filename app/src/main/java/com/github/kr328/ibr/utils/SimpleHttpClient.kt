package com.github.kr328.ibr.utils

import java.net.HttpURLConnection
import java.net.URL

object SimpleHttpClient {
    fun get(url: URL): String {
        with ( url.openConnection() as HttpURLConnection ) {
            connectTimeout = 10 * 1000
            readTimeout = 10 * 1000

            return inputStream.bufferedReader().readLines().joinToString()
        }
    }
}