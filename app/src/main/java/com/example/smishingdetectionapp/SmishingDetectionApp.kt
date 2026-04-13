package com.example.smishingdetectionapp

import android.app.Application
import com.example.smishingdetectionapp.di.initKoin

class SmishingDetectionApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // initialise Koin with the application context
        initKoin(this)
    }
}