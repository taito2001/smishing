package com.example.smishingdetectionapp.domain.riskscanner

// Platform-specific checks. Each platform implements this differently.
interface RiskCheckProvider {
    fun hasSuspiciousSms(): Boolean
    fun hasSecurityApp(): Boolean
    fun hasSpamFilter(): Boolean
    fun isDeviceSecured(): Boolean
    fun hasUnknownSourcesEnabled(): Boolean
    fun hasTrustedSmsApp(): Boolean
}