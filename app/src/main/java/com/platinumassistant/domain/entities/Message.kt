package com.platinumassistant.domain.entities

/**
 * Entity representing a Chat message
 */
data class Message(
    val id: String,
    val content: String,
    val sender: MessageSender,
    val timestamp: Long = System.currentTimeMillis(),
    val personalityId: String? = null,
    val attachments: List<String> = emptyList(),
    val isSynthesized: Boolean = false,
)

enum class MessageSender {
    USER,
    ASSISTANT
}
