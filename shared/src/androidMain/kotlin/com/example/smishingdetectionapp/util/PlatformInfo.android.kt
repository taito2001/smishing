package com.example.smishingdetectionapp.util

import android.os.Build
import android.content.Context
import com.example.smishingdetectionapp.platform.AndroidContextHolder

actual fun getPlatformInfo(): PlatformInfo {
    val context = AndroidContextHolder.appContext
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return PlatformInfo(
        platformName = "Android",
        osVersion = Build.VERSION.RELEASE,
        deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}",
        appVersion = packageInfo.versionName ?: "unknown"
    )
}

// overloaded version that accepts context for app version
fun getPlatformInfo(context: Context): PlatformInfo {
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return PlatformInfo(
        platformName = "Android",
        osVersion = Build.VERSION.RELEASE,
        deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}",
        appVersion = packageInfo.versionName ?: "unknown"
    )
}
