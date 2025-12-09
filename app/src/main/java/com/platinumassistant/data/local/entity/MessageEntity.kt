package com.platinumassistant.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.platinumassistant.domain.entities.Message
import com.platinumassistant.domain.entities.MessageSender

/**
 * Room entity for Message
 * Represents a chat message stored in the local database
 */
@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val content: String,
    val sender: String, // "USER" or "ASSISTANT"
    val timestamp: Long,
    val personalityId: String? = null,
    val language: String = "ar",
    val audioFilePath: String? = null,
    val emotionalTone: String? = null,
    val confidence: Float = 1.0f,
    val metadata: String = "{}" // JSON string
) {
    // Convert to domain model
    fun toDomainModel(): Message {
        return Message(
            id = id,
            content = content,
            sender = if (sender == "USER") MessageSender.USER else MessageSender.ASSISTANT,
            timestamp = timestamp,
            personalityId = personalityId,
            attachments = emptyList(),
            language = language,
            audioFilePath = audioFilePath,
            emotionalTone = emotionalTone,
            confidence = confidence
        )
    }

    companion object {
        // Convert from domain model
        fun fromDomainModel(message: Message): MessageEntity {
            return MessageEntity(
                id = message.id,
                content = message.content,
                sender = if (message.sender == MessageSender.USER) "USER" else "ASSISTANT",
                timestamp = message.timestamp,
                personalityId = message.personalityId,
                language = message.language,
                audioFilePath = message.audioFilePath,
                emotionalTone = message.emotionalTone,
                confidence = message.confidence
            )
        }
    }
}
