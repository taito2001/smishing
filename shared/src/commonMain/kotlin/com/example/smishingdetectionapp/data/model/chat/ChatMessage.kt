package com.example.smishingdetectionapp.data.model.chat

import kotlinx.serialization.Serializable
import com.example.smishingdetectionapp.util.currentTimeMillis

@Serializable
data class ChatMessage(
    val id: Long = 0,
    val sender: String,
    val text: String,
    val timestamp: Long
) {
    companion object {
        const val USER = "USER"
        const val BOT = "BOT"

        fun userMessage(text: String, timestamp: Long = currentTimeMillis()): ChatMessage {
            return ChatMessage(sender = USER, text = text, timestamp = timestamp)
        }

        fun botMessage(text: String, timestamp: Long = currentTimeMillis()): ChatMessage {
            return ChatMessage(sender = BOT, text = text, timestamp = timestamp)
        }
    }

    val isFromUser: Boolean
        get() = sender == USER
}