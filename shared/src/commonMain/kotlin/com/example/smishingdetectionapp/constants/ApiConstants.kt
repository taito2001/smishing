package com.example.smishingdetectionapp.constants

object ApiConstants {

    // Base URLS
    const val BASE_URL = ""
    const val OLLAMA_BASE_URL = ""
    const val RSS_FEED_URL = ""

    // Endpoints
    const val LOGIN_ENDPOINT = ""
    const val REGISTER_ENGPOINT = ""
    const val FAQ_ENDPOINT = ""
    const val COMMUNITY_ENDPOINT = ""
    const val NEWS_ENDPOINT = ""

    // Timeouts (in milliseconds)
    const val CONNECT_TIMEOUT = 10_000L
    const val READ_TIMEOUT = 15_000L
    const val WRITE_TIMEOUT = 15_000L

    // Retry
    const val MAX_RETRIES = 3
    const val RETRY_DELAY_MS = 1_000L

    // Ollama
    const val OLLAMA_MODEL = ""
    const val OLLAMA_TIMEOUT = 60_000L
}