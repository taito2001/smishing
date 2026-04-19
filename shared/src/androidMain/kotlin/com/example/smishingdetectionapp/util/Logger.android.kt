package com.example.smishingdetectionapp.util

import android.util.Log

actual fun logDebug(tag: String, message: String) { Log.d(tag, message) }
actual fun logError(tag: String, message: String) { Log.e(tag, message) }
actual fun logWarning(tag: String, message: String) { Log.w(tag, message) }
