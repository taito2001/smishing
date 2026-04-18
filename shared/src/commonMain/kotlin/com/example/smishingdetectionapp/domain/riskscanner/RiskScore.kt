package com.example.smishingdetectionapp.domain.riskscanner

// Holds the result of a risk scan
data class RiskScore (
    val totalScore: Int,
    val triggeredRisks: List<String>,
    val checkResults: List<RiskCheckResult>
) {

    val riskLevel: String
        get() = when {
            totalScore <= 30 -> "Low Risk"
            totalScore <= 60 -> "Moderate Risk"
            else -> "High Risk"
        }

    // List of possible recommendations
    val recommendations: List<String>
        get() = if (totalScore > 30) {
            listOf(
                "Set up a strong password or PIN to protect your data.",
                "Install antivirus or security apps to detect suspicious activity.",
                "Disable installations from unknown sources.",
                "Enable a spam filter to block smishing messages",
                "Avoid clicking links in SMS from unknown senders.",
                "Report suspicious SMS to your mobile provider/organisation.",
                "Take a cybersecurity awareness course."
            )
        } else {
            listOf("Keep up your excellent security habits! You're doing great.")
        }
}

// result of a single risk check
data class RiskCheckResult(
    val name: String,
    val passed: Boolean,
    val riskPoints: Int,
    val failureMessage: String
)
