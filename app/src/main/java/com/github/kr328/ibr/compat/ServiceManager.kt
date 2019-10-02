package com.github.kr328.ibr.compat

import android.os.IBinder
import java.lang.reflect.Method

object ServiceManager {
    private val methodGetService: Method by lazy {
        Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String::class.java).apply {
            isAccessible = true
        }
    }

    fun getService(name: String): IBinder {
        return methodGetService.invoke(null, name) as IBinder
    }
}

