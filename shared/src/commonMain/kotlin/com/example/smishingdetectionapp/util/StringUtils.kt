package com.example.smishingdetectionapp.util

object StringUtils {

    fun containsUrl(text: String): Boolean {
        return Regex("https?://\\S+").containsMatchIn(text)
    }

    fun extractUrls(text: String): List<String> {
        return Regex("https?://\\S+").findAll(text).map {it.value}.toList()
    }


    fun containsSuspiciousKeywords(text: String): Boolean {
        val keywords = listOf(
            "urgent", "verify your account", "click here",
            "your account has been", "suspended", "unusual activity",
            "confirm your identity", "win", "prize", "claim now",
            "limited time", "act now", "free gift"
        )
        val lowerText = text.lowercase()
        return keywords.any { lowerText.contains(it) }
    }

    fun stripHtml(html: String): String {
        return Regex("<[^>]*>").replace(html, "")
    }
}
