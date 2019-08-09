package com.github.kr328.ibr.controller

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException

fun PackageManager.getApplicationInfoOrNull(packageName: String): ApplicationInfo? {
    return try {
        getApplicationInfo(packageName, 0)

    } catch (e: NameNotFoundException) {
        null
    }
}

fun PackageManager.getPackageInfoOrNull(packageName: String): PackageInfo? {
    return try {
        this.getPackageInfo(packageName, 0)
    } catch (e: NameNotFoundException) {
        null
    }
}