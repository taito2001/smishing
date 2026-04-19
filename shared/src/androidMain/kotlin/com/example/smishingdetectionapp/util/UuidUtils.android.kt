package com.example.smishingdetectionapp.util

import java.util.UUID

actual fun generateUUID(): String = UUID.randomUUID().toString()