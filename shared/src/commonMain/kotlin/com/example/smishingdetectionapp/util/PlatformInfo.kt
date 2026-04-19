package com.example.smishingdetectionapp.util

data class PlatformInfo(
    val platformName: String,
    val osVersion: String,
    val deviceModel: String,
    val appVersion: String
)

expect fun getPlatformInfo(): PlatformInfo