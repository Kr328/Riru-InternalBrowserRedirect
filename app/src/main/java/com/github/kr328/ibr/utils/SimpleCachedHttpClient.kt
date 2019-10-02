package com.github.kr328.ibr.utils

import com.github.kr328.ibr.Constants
import java.io.File
import java.net.URL

class SimpleCachedHttpClient(private val cacheDir: File, var baseUrl: String) {
    init {
        cacheDir.mkdirs()
    }

    @Synchronized
    fun get(file: String, cacheFirst: Boolean, ignoreCache: Boolean): String {
        val targetFile = cacheDir.resolve(file)

        if (targetFile.exists() && cacheFirst)
            return targetFile.readText()

        if (!targetFile.exists() || ignoreCache || System.currentTimeMillis() - targetFile.lastModified() > Constants.DEFAULT_RULE_INVALIDATE ) {
            val data = SimpleHttpClient.get(URL("$baseUrl/$file"))

            targetFile.writeText(data)

            return data;
        }

        return targetFile.readText()
    }
}