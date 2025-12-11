package com.platinumassistant.domain.usecases

import com.platinumassistant.domain.entities.Message
import com.platinumassistant.domain.repositories.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Container class for all message-related use cases
 * Simplifies injection and grouping of related use cases
 */
class MessageUseCases @Inject constructor(
    val sendMessage: SendMessageUseCase,
    val getHistory: GetMessageHistoryUseCase,
    val getRecent: GetRecentMessagesUseCase,
    val deleteMessage: DeleteMessageUseCase,
    val clearHistory: ClearChatHistoryUseCase,
    val getMessageCount: GetMessageCountUseCase
)

/**
 * Use case for sending a message
 */
class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(message: Message) {
        messageRepository.sendMessage(message)
    }
}

/**
 * Use case for getting chat history
 */
class GetMessageHistoryUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(): Flow<List<Message>> {
        return messageRepository.getMessageHistory()
    }
}

/**
 * Use case for getting recent messages
 */
class GetRecentMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(limit: Int = 50): Flow<List<Message>> {
        return messageRepository.getRecentMessages(limit)
    }
}

/**
 * Use case for deleting a message
 */
class DeleteMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(messageId: String): Flow<Boolean> {
        return flowOf(messageRepository.deleteMessage(messageId))
    }
}

/**
 * Use case for clearing chat history
 */
class ClearChatHistoryUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke() {
        messageRepository.clearChatHistory()
    }
}

/**
 * Use case for getting message count
 */
class GetMessageCountUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(): Int {
        return messageRepository.getMessageCount()
    }
}
