package com.example.smishingdetectionapp.util

object ValidationUtils {

    // EMAIL Validation
    fun isValidEmail(email: String): Boolean {
        return Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(email)
    }

    fun getEmailError(email: String): String? {
        if (email.isBlank()) return "Email is required"
        if (!email.contains("@")) return "Email must contain @"
        if (!email.contains(".")) return "Email must contain a domain"
        if (!isValidEmail(email)) return "Invalid email format"
        return null
    }

    //PIN Validation
    fun isValidPin(pin: String): Boolean {
        return pin.length in 4..6 && pin.all { it.isDigit() }
    }

    fun getPinError(pin: String): String? {
        if (pin.isBlank()) return "PIN is required"
        if (!pin.all { it.isDigit() }) return "PIN must contain only digits"
        if (pin.length < 4) return "PIN must be at least 4 digits"
        if (pin.length > 6) return "PIN must be at most 6 digits"
        return null
    }

    // PASSWORD Validation
    data class PasswordStrength(
        val score: Int,         // 0-4
        val label: String,      // "Weak", "Moderate", "Strong", "Very Strong"
        val checks: List<PasswordCheck>
    )

    data class PasswordCheck(
        val description: String,
        val passed: Boolean
    )

    fun getPasswordStrength(password: String): PasswordStrength {
        val checks = listOf(
            PasswordCheck("At least 8 characters", password.length >= 8),
            PasswordCheck("Contains uppercase letter", password.any { it.isUpperCase() }),
            PasswordCheck("Contains lowercase letter", password.any { it.isLowerCase() }),
            PasswordCheck("Contains a digit", password.any { it.isDigit() }),
            PasswordCheck("Contains special character", password.any { !it.isLetterOrDigit() })
        )

        val score = checks.count { it.passed}

        val label = when (score) {
            0, 1 -> "Weak"
            2 -> "Moderate"
            3 -> "Strong"
            4, 5 -> "Very Strong"
            else -> "Weak"
        }
        return PasswordStrength(score, label, checks)
    }

    fun isValidPassword(password: String): Boolean {
        val strength = getPasswordStrength(password)
        return strength.score >= 3
    }

    fun getPasswordError(password: String): String? {
        if (password.isBlank()) return "Password is required"
        val strength = getPasswordStrength(password)
        if (strength.score < 3) {
            val failed = strength.checks.filter { !it.passed }
            return failed.joinToString("\n") {"• ${it.description}"}
        }
        return null
    }

    // NAME Validation
    fun isValidName(name: String): Boolean {
        return name.trim().length >= 2 && name.all { it.isLetter() || it.isWhitespace() || it == '-' || it == '\''}
    }

    fun getNameError(name: String): String? {
        if (name.isBlank()) return "Name is required"
        if (name.trim().length < 2) return "Name must be at least 2 characters"
        if (!name.all { it.isLetter() || it.isWhitespace() || it == '-' || it == '\''}) {
            return "Name can only contain letters, spaces, hyphens and apostrophes"
        }
        return null
    }


}