package com.platinumassistant.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.platinumassistant.domain.entities.Message
import com.platinumassistant.domain.entities.Personality
import com.platinumassistant.domain.repositories.MessageRepository
import com.platinumassistant.domain.repositories.PersonalityRepository
import com.platinumassistant.domain.usecases.PersonalityUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for Chat Screen
 * Manages message state, chat history, and personality selection
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val personalityRepository: PersonalityRepository,
    private val personalityUseCases: PersonalityUseCases
) : ViewModel() {
    
    // Chat State
    private val _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()
    
    // UI State
    private val _messageInput = MutableStateFlow("")
    val messageInput: StateFlow<String> = _messageInput.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        loadChatHistory()
        loadSelectedPersonality()
    }
    
    /**
     * Load chat history from repository
     */
    private fun loadChatHistory() {
        viewModelScope.launch {
            try {
                messageRepository.getMessageHistory().collect { messages ->
                    _chatState.update { currentState ->
                        currentState.copy(
                            messages = messages.sortedBy { it.timestamp }
                        )
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error loading chat history")
                _errorMessage.value = "Failed to load chat history: ${e.message}"
            }
        }
    }
    
    /**
     * Load currently selected personality
     */
    private fun loadSelectedPersonality() {
        viewModelScope.launch {
            try {
                val selected = personalityRepository.getSelectedPersonality()
                _chatState.update { currentState ->
                    currentState.copy(selectedPersonality = selected)
                }
                Timber.d("Loaded personality: ${selected?.name ?: "None"}")
            } catch (e: Exception) {
                Timber.e(e, "Error loading selected personality")
            }
        }
    }
    
    /**
     * Send a message
     * @param content Message text to send
     */
    fun sendMessage(content: String) {
        if (content.isBlank()) {
            _errorMessage.value = "Message cannot be empty"
            return
        }
        
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Create user message
                val userMessage = Message(
                    id = UUID.randomUUID().toString(),
                    content = content,
                    sender = "user",
                    timestamp = System.currentTimeMillis(),
                    language = "en",
                    isUserMessage = true
                )
                
                // Persist user message
                messageRepository.sendMessage(userMessage)
                _messageInput.value = ""
                
                // Simulate assistant response (will be replaced with real AI)
                generateAssistantResponse(userMessage)
                
                _errorMessage.value = null
            } catch (e: Exception) {
                Timber.e(e, "Error sending message")
                _errorMessage.value = "Failed to send message: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Generate assistant response (placeholder for AI integration)
     * @param userMessage The message to respond to
     */
    private suspend fun generateAssistantResponse(userMessage: Message) {
        try {
            // TODO: Integrate with actual AI model (Whisper/BERT/LLM)
            val personality = _chatState.value.selectedPersonality
            
            val responseText = when {
                userMessage.content.contains("hello", ignoreCase = true) ->
                    "Hello! I'm ${personality?.name ?: "your assistant"}. How can I help you?"
                userMessage.content.contains("how are you", ignoreCase = true) ->
                    "I'm doing great! Thanks for asking. What can I help you with?"
                userMessage.content.contains("bye", ignoreCase = true) ->
                    "Goodbye! Feel free to come back anytime."
                else ->
                    "I understand. How can I assist you further?"
            }
            
            // Create assistant message
            val assistantMessage = Message(
                id = UUID.randomUUID().toString(),
                content = responseText,
                sender = personality?.name ?: "Assistant",
                timestamp = System.currentTimeMillis() + 100, // Slight delay for realism
                language = personality?.language ?: "en",
                isUserMessage = false
            )
            
            // Persist assistant message
            messageRepository.sendMessage(assistantMessage)
        } catch (e: Exception) {
            Timber.e(e, "Error generating assistant response")
            _errorMessage.value = "Failed to generate response: ${e.message}"
        }
    }
    
    /**
     * Update message input
     * @param text New input text
     */
    fun updateMessageInput(text: String) {
        _messageInput.value = text
    }
    
    /**
     * Select a personality
     * @param personalityId ID of personality to select
     */
    fun selectPersonality(personalityId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                personalityUseCases.selectPersonality(personalityId).collect { success ->
                    if (success) {
                        loadSelectedPersonality()
                        _errorMessage.value = null
                        Timber.d("Personality selected: $personalityId")
                    } else {
                        _errorMessage.value = "Failed to select personality"
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error selecting personality")
                _errorMessage.value = "Failed to select personality: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Clear chat history
     */
    fun clearChatHistory() {
        viewModelScope.launch {
            try {
                messageRepository.clearChatHistory()
                _chatState.update { it.copy(messages = emptyList()) }
                Timber.d("Chat history cleared")
            } catch (e: Exception) {
                Timber.e(e, "Error clearing chat history")
                _errorMessage.value = "Failed to clear chat history: ${e.message}"
            }
        }
    }
    
    /**
     * Delete a specific message
     * @param messageId ID of message to delete
     */
    fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            try {
                val deleted = messageRepository.deleteMessage(messageId)
                if (deleted) {
                    Timber.d("Message deleted: $messageId")
                } else {
                    _errorMessage.value = "Message not found"
                }
            } catch (e: Exception) {
                Timber.e(e, "Error deleting message")
                _errorMessage.value = "Failed to delete message: ${e.message}"
            }
        }
    }
    
    /**
     * Dismiss error message
     */
    fun dismissError() {
        _errorMessage.value = null
    }
}

/**
 * State class for Chat Screen
 */
data class ChatState(
    val messages: List<Message> = emptyList(),
    val selectedPersonality: Personality? = null,
    val messageCount: Int = 0
)
