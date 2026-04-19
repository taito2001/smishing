package com.example.smishingdetectionapp.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.smishingdetectionapp.SmishingDatabase
import com.example.smishingdetectionapp.data.model.chat.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ChatRepositoryImpl(private val database: SmishingDatabase) : ChatRepository {

    private val queries = database.chatMessageQueries

    override fun getAllMessages(): Flow<List<ChatMessage>> {
        return queries.getAllMessages()
            .asFlow()
            .mapToList(Dispatchers.IO)
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
            .mapToList(Dispatchers.IO)
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
        return withContext(Dispatchers.IO) {
            queries.insertMessage(
                sender = message.sender,
                text = message.text,
                timestamp = message.timestamp
            )
            queries.transactionWithResult {
                queries.getLastInsertRowId().executeAsOne()
            }
        }
    }

    override suspend fun deleteMessage(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteMessageById(id)
        }
    }

    override suspend fun clearAllMessages() {
        withContext(Dispatchers.IO) {
            queries.deleteAllMessages()
        }
    }

    override suspend fun getMessageById(id: Long): ChatMessage? {
        return withContext(Dispatchers.IO) {
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
