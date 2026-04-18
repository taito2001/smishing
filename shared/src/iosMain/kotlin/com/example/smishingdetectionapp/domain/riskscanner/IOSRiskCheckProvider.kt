package com.example.smishingdetectionapp.domain.riskscanner

import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthentication


@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
class IOSRiskCheckProvider : RiskCheckProvider {
    override fun hasSuspiciousSms(): Boolean {
        // No Implementation
        // iOS doesn't allow reading SMS
        return false
    }

    override fun hasSecurityApp(): Boolean {
        // No Implementation
        // iOS doesn't expose installed apps — return false or prompt user
        return false
    }

    override fun hasSpamFilter(): Boolean {
        // iOS 10+ supports SMS filtering extensions
        // No programmatic way to detect — assume true if on iOS 17+
        return true
    }

    override fun isDeviceSecured(): Boolean {
        val context = LAContext()
        return context.canEvaluatePolicy(
            LAPolicyDeviceOwnerAuthentication,
            error = null
        )
    }

    override fun hasUnknownSourcesEnabled(): Boolean {
        // No Implementation
        // iOS doesn't allow sideloading — always safe
        return false
    }

    override fun hasTrustedSmsApp(): Boolean {
        // No Implementation
        // Only Messages app exists — always trusted
        return true
    }

}