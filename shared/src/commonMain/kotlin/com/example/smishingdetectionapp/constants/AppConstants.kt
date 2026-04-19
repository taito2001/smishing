package com.example.smishingdetectionapp.constants

object AppConstants {

    // App Info
    const val APP_NAME = "Smishing Detection App"

    // Database
    const val DB_NAME = "smishing.db"
    const val DETECTION_DB_NAME = "detectlist.db"

    // Preferences
    const val PREFS_NAME = "app_settings"
    const val KEY_USER_AGE = "user_age"
    const val KEY_FIRST_RUN = "first_run"
    const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"

    // Scan Defaults
    const val DEFAULT_USER_AGE = 0
    const val SCAN_ANIMATION_DURATION_MS = 9_000L

    // Limits
    const val MAX_CHAT_HISTORY = 100
    const val MAX_NEWS_ARTICLES = 50
    const val MAX_COMMUNITY_POSTS = 100
}