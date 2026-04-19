package com.example.smishingdetectionapp.util

import platform.Foundation.NSBundle
import platform.UIKit.UIDevice

actual fun getPlatformInfo(): PlatformInfo {
    return PlatformInfo(
        platformName = "iOS",
        osVersion = UIDevice.currentDevice.systemVersion,
        deviceModel = UIDevice.currentDevice.model,
        appVersion = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as?
                String ?: "unknown"
    )
}