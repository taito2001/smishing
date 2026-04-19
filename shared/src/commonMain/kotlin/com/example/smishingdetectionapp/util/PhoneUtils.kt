package com.example.smishingdetectionapp.util

object PhoneUtils {

    private val countryCodes = mapOf(
        "+44" to "UK",
        "+353" to "Ireland",
        "+1" to "US/Canada",
        "+61" to "Australia",
        "+49" to "Germany",
        "+33" to "France",
        "+39" to "Italy",
        "+34" to "Spain",
        "+81" to "Japan",
        "+86" to "China",
        "+91" to "India",
        "+55" to "Brazil",
        "+52" to "Mexico",
        "+27" to "South Africa",
        "+31" to "Netherlands",
        "+32" to "Belgium",
        "+46" to "Sweden",
        "+47" to "Norway",
        "+45" to "Denmark",
        "+358" to "Finland"
    )

    fun getCountryFromNumber(phone: String): String? {
        val cleaned = phone.replace(Regex("[\\s\\-\\(\\)]"), "")
        return countryCodes.entries
            .sortedByDescending { it.key.length }
            .firstOrNull { cleaned.startsWith(it.key) }
            ?.value
    }

    fun normalizeNumber(phone: String): String {
        return phone.replace(Regex("[\\s\\-\\(\\)\\.]"), "")
    }

    fun formatNumber(phone: String, countryCode: String = "+44"): String {
        val cleaned = normalizeNumber(phone)
        return if (cleaned.startsWith("+")) {
            cleaned
        } else if (cleaned.startsWith("0")) {
            countryCode + cleaned.substring(1)
        } else {
            countryCode + cleaned
        }
    }

    fun isShortCode(phone: String): Boolean {
        val cleaned = normalizeNumber(phone)
        return cleaned.length <= 5
    }

    fun isSuspiciousSender(sender: String): Boolean {
        val legitimateShortCodes = listOf(
            "GOOGLE", "AMAZON", "APPLE", "FACEBOOK",
            "PAYPAL", "HSBC", "BARCLAYS", "NATWEST"
        )

        if (legitimateShortCodes.any { sender.uppercase().contains(it) }) {
            return false
        }

        if (sender.length <= 5 && sender.all { it.isDigit() }) {
            return true
        }

        if (sender.any { it.isLetter() } && sender.any { it.isDigit() }) {
            return true
        }

        return false
    }
}
