package com.github.kr328.ibr.compat

object SystemProperties {
    private val methodGet by lazy {
        Class.forName("android.os.SystemProperties")
                .getMethod("get", String::class.java, String::class.java)
                .apply { isAccessible = true }
    }

    fun get(key: String, fallback: String): String {
        return try {
            methodGet.invoke(null, key, fallback) as String
        } catch (e: ReflectiveOperationException) {
            fallback
        }
    }
}