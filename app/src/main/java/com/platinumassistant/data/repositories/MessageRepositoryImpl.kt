package com.platinumassistant.data.repositories

import com.platinumassistant.data.local.dao.MessageDao
import com.platinumassistant.data.local.entity.MessageEntity
import com.platinumassistant.domain.entities.Message
import com.platinumassistant.domain.repositories.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of MessageRepository
 * Handles all message-related data operations including persistence and retrieval
 */
class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao
) : MessageRepository {
    
    override suspend fun sendMessage(message: Message) {
        messageDao.insertMessage(MessageEntity.fromDomainModel(message))
    }
    
    override fun getMessageHistory(): Flow<List<Message>> =
        messageDao.getAllMessages().map { entities ->
            entities.map { it.toDomainModel() }
        }
    
    override fun getRecentMessages(limit: Int): Flow<List<Message>> =
        messageDao.getRecentMessages(limit).map { entities ->
            entities.map { it.toDomainModel() }
        }
    
    override suspend fun getMessage(id: String): Message? =
        messageDao.getMessageById(id)?.toDomainModel()
    
    override suspend fun updateMessage(message: Message) {
        messageDao.updateMessage(MessageEntity.fromDomainModel(message))
    }
    
    override suspend fun deleteMessage(id: String): Boolean = try {
        val message = messageDao.getMessageById(id)
        if (message != null) {
            messageDao.deleteMessage(message)
            true
        } else {
            false
        }
    } catch (e: Exception) {
        false
    }
    
    override suspend fun clearChatHistory() {
        messageDao.clearAllMessages()
    }
    
    override suspend fun deleteMessagesOlderThan(days: Int): Boolean = try {
        val cutoffTime = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L)
        messageDao.deleteMessagesOlderThan(cutoffTime)
        true
    } catch (e: Exception) {
        false
    }
    
    override suspend fun getMessageCount(): Int =
        messageDao.getMessageCount()
}
