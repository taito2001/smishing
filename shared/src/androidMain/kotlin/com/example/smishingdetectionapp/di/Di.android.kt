package com.example.smishingdetectionapp.di

import android.content.Context
import org.koin.core.module.Module
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import com.example.smishingdetectionapp.SmishingDatabase
import com.example.smishingdetectionapp.data.repository.ChatRepository
import com.example.smishingdetectionapp.data.repository.ChatRepositoryImpl
import com.example.smishingdetectionapp.platform.DatabaseDriverFactory


actual fun platformModule(): Module = module {
    single<SmishingDatabase> {
        val driver = DatabaseDriverFactory(androidContext()).createDriver()
        SmishingDatabase(driver)

    }

    single<ChatRepository> { ChatRepositoryImpl(get()) }
}

// Initialise Koin on Android (pass Application context)
actual fun initKoin(context: Any?) {
    val appContext = context as? Context
        ?: throw IllegalArgumentException("Android initKoin requires a Context")
    startKoin {
        androidContext(appContext)
        modules(listOf(sharedModule, platformModule()))
    }
}

