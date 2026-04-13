package com.example.smishingdetectionapp.data.repository

import com.example.smishingdetectionapp.data.model.chat.ChatMessage
import kotlinx.coroutines.flow.Flow


// Repository interface for chat message operations
// implementations will be platform-specific
interface ChatRepository {

    // Get all chat messages as a "Flow" (asynchronous data stream)
    fun getAllMessages(): Flow<List<ChatMessage>>

    //Get Messages by sender type
    fun getMessagesBySender(sender: String): Flow<List<ChatMessage>>

    // Save a new chat message
    suspend fun saveMessage(message: ChatMessage): Long

    //Delete a message by ID
    suspend fun deleteMessage(id: Long)

    //Delete all messages (clear chat history)
    suspend fun clearAllMessages()

    // Get a single message by ID
    suspend fun getMessageById(id: Long): ChatMessage?

}