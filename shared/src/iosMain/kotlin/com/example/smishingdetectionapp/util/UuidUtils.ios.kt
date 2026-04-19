package com.example.smishingdetectionapp.util

import platform.Foundation.NSUUID

actual fun generateUUID(): String = NSUUID().UUIDString()