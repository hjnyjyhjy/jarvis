package com.platinumassistant.domain.repositories

import com.platinumassistant.domain.entities.Message
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Message operations
 * Provides abstraction for message persistence layer
 */
interface MessageRepository {
    
    /**
     * Send a message (user input)
     */
    suspend fun sendMessage(message: Message)
    
    /**
     * Get complete chat history
     */
    fun getMessageHistory(): Flow<List<Message>>
    
    /**
     * Get recent messages (limited by count)
     */
    fun getRecentMessages(limit: Int): Flow<List<Message>>
    
    /**
     * Get a specific message by ID
     */
    suspend fun getMessage(id: String): Message?
    
    /**
     * Update an existing message
     */
    suspend fun updateMessage(message: Message)
    
    /**
     * Delete a message by ID
     */
    suspend fun deleteMessage(id: String): Boolean
    
    /**
     * Clear entire chat history
     */
    suspend fun clearChatHistory()
    
    /**
     * Delete messages older than specified days
     */
    suspend fun deleteMessagesOlderThan(days: Int): Boolean
    
    /**
     * Get total message count
     */
    suspend fun getMessageCount(): Int
}
