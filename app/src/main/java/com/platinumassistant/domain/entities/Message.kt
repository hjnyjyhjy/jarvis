package com.platinumassistant.domain.entities

import java.util.UUID

/**
 * Entity representing a Chat message with comprehensive metadata
 */
data class Message(
    val id: String = UUID.randomUUID().toString(),
    val content: String = "",
    val sender: MessageSender = MessageSender.USER,
    val timestamp: Long = System.currentTimeMillis(),
    val personalityId: String? = null,
    val attachments: List<String> = emptyList(),
    val isSynthesized: Boolean = false,
    val language: String = "ar",
    val audioFilePath: String? = null,
    val emotionalTone: String? = null,
    val confidence: Float = 1.0f,
    val metadata: Map<String, String> = emptyMap()
) {
    fun isUserMessage(): Boolean = sender == MessageSender.USER
    fun isAssistantMessage(): Boolean = sender == MessageSender.ASSISTANT
}

enum class MessageSender {
    USER,
    ASSISTANT
}
