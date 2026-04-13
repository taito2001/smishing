package com.example.smishingdetectionapp.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import com.example.smishingdetectionapp.SmishingDatabase
import com.example.smishingdetectionapp.data.repository.ChatRepository
import com.example.smishingdetectionapp.data.repository.ChatRepositoryImpl
import com.example.smishingdetectionapp.platform.DatabaseDriverFactory

actual fun platformModule(): Module = module {
    // iOS specific bindings (no context)
    single<SmishingDatabase> {
        val driver = DatabaseDriverFactory().createDriver()
        SmishingDatabase(driver)
    }

    single<ChatRepository> { ChatRepositoryImpl(get()) }
}

// Initialise Koin on iOS
actual fun initKoin(context: Any?) {
    //no context needed
    startKoin {
        modules(listOf(sharedModule, platformModule()))
    }
}

