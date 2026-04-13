package com.example.smishingdetectionapp.di

import com.example.smishingdetectionapp.data.repository.ChatRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object KoinHelper : KoinComponent {
    fun getChatRepository(): ChatRepository = get()
}