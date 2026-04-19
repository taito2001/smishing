package com.example.smishingdetectionapp.util

actual fun logDebug(tag: String, message: String) = println("DEBUG [$tag]: $message")
actual fun logError(tag: String, message: String) = println("ERROR [$tag]: $message")
actual fun logWarning(tag: String, message: String) = println("WARN [$tag]: $message")
