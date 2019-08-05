package com.github.kr328.ibr.utils

object ExceptionUtils {
    fun <R> fallback(runnable: () -> R, def: R): R {
        return try {
            runnable.invoke()
        } catch (ignored: Exception) {
            def
        }
    }
}