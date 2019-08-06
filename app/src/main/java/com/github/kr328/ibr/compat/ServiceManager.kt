package com.github.kr328.ibr.compat

import android.os.IBinder

fun connectSystemService(name: String): IBinder {
    return with ( Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String::class.java) ) {
        isAccessible = true
        invoke(null, name)
    } as IBinder
}