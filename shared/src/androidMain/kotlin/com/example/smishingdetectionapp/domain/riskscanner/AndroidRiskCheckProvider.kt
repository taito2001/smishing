package com.example.smishingdetectionapp.domain.riskscanner

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.provider.Settings

class AndroidRiskCheckProvider(private val context: Context) : RiskCheckProvider {

    private val knownSecurityApps = listOf(
        "com.norton.mobilesecurity",
        "com.mcafee.android",
        "com.bitdefender.antivirus"
    )

    private val knownSpamFilters = listOf(
        "com.google.android.apps.messaging",
        "com.mrnumber.blocker",
        "com.truecaller"
    )


    override fun hasSuspiciousSms(): Boolean {
        //Unimplemented
        return false


    }

    override fun hasSecurityApp(): Boolean {
        val pm = context.packageManager
        return knownSecurityApps.any {app ->
            try {
                pm.getPackageInfo(app, 0)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    override fun hasSpamFilter(): Boolean {
        val pm = context.packageManager
        return knownSpamFilters.any {app ->
            try {
                pm.getPackageInfo(app, 0)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    override fun isDeviceSecured(): Boolean {
        val km = context.getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
        return km?.isDeviceSecure == true
    }

    override fun hasUnknownSourcesEnabled(): Boolean {
        // check if SmishingDetectionApp can install unknown apps
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.packageManager.canRequestPackageInstalls()
        } else {
            try {
                Settings.Secure.getInt(
                    context.contentResolver,
                    Settings.Secure.INSTALL_NON_MARKET_APPS
                ) == 1
            } catch (e: Exception) {
                false
            }
        }
    }

    override fun hasTrustedSmsApp(): Boolean {
        val defaultSmsApp = Settings.Secure.getString(
            context.contentResolver,
            "sms_default_application"
        )
        return defaultSmsApp != null && (
                defaultSmsApp.contains("messages") || defaultSmsApp.contains("samsung")
        )
    }
}