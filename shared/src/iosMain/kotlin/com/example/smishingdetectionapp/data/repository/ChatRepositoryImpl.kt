package com.example.smishingdetectionapp.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.smishingdetectionapp.SmishingDatabase
import com.example.smishingdetectionapp.data.model.chat.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ChatRepositoryImpl(
    private val database: SmishingDatabase,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : ChatRepository {

    private val queries = database.chatMessageQueries  // <-- fixed

    override fun getAllMessages(): Flow<List<ChatMessage>> {
        return queries.getAllMessages()
            .asFlow()
            .mapToList(dispatcher)
            .map { entities ->
                entities.map { entity ->
                    ChatMessage(
                        id = entity.id,
                        sender = entity.sender,
                        text = entity.text,
                        timestamp = entity.timestamp
                    )
                }
            }
    }

    override fun getMessagesBySender(sender: String): Flow<List<ChatMessage>> {
        return queries.getMessagesBySender(sender)
            .asFlow()
            .mapToList(dispatcher)
            .map { entities ->
                entities.map { entity ->
                    ChatMessage(
                        id = entity.id,
                        sender = entity.sender,
                        text = entity.text,
                        timestamp = entity.timestamp
                    )
                }
            }
    }

    override suspend fun saveMessage(message: ChatMessage): Long {
        return withContext(dispatcher) {
            queries.insertMessage(
                sender = message.sender,
                text = message.text,
                timestamp = message.timestamp
            )
            queries.getLastInsertRowId().executeAsOne()
        }
    }

    override suspend fun deleteMessage(id: Long) {
        withContext(dispatcher) {
            queries.deleteMessageById(id)
        }
    }

    override suspend fun clearAllMessages() {
        withContext(dispatcher) {
            queries.deleteAllMessages()
        }
    }

    override suspend fun getMessageById(id: Long): ChatMessage? {
        return withContext(dispatcher) {
            queries.getMessageById(id).executeAsOneOrNull()?.let { entity ->
                ChatMessage(
                    id = entity.id,
                    sender = entity.sender,
                    text = entity.text,
                    timestamp = entity.timestamp
                )
            }
        }
    }
}