package com.github.kr328.ibr.utils

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object SimpleHttpClient {
    fun get(url: URL): String {
        with ( url.openConnection() as HttpURLConnection ) {
            connectTimeout = 10 * 1000
            readTimeout = 10 * 1000
            instanceFollowRedirects = true

            connect()

            if ( responseCode / 100 != 2 )
                throw IOException()

            val result = inputStream.bufferedReader().readLines().joinToString("")

            disconnect()

            return result
        }
    }
}