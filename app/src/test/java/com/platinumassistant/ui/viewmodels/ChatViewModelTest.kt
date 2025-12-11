package com.platinumassistant.ui.viewmodels

import app.cash.turbine.test
import com.platinumassistant.domain.entities.Message
import com.platinumassistant.domain.entities.Personality
import com.platinumassistant.domain.repositories.MessageRepository
import com.platinumassistant.domain.repositories.PersonalityRepository
import com.platinumassistant.domain.usecases.PersonalityUseCases
import com.platinumassistant.domain.usecases.GetAllPersonalitiesUseCase
import com.platinumassistant.domain.usecases.GetPersonalitiesByCategoryUseCase
import com.platinumassistant.domain.usecases.GetFavoritePersonalitiesUseCase
import com.platinumassistant.domain.usecases.SelectPersonalityUseCase
import com.platinumassistant.domain.usecases.ToggleFavoritePersonalityUseCase
import com.platinumassistant.domain.usecases.GetTrendingPersonalitiesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID

// Lightweight fake repositories for unit testing without Android
class FakeMessageRepository : MessageRepository {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    override suspend fun sendMessage(message: Message) {
        _messages.value = _messages.value + message
    }

    override fun getMessageHistory(): Flow<List<Message>> = _messages

    override fun getRecentMessages(limit: Int): Flow<List<Message>> = flow { emit(_messages.value.takeLast(limit)) }

    override suspend fun getMessage(id: String): Message? = _messages.value.find { it.id == id }

    override suspend fun updateMessage(message: Message) {
        _messages.value = _messages.value.map { if (it.id == message.id) message else it }
    }

    override suspend fun deleteMessage(id: String): Boolean {
        val before = _messages.value
        _messages.value = before.filterNot { it.id == id }
        return before.size != _messages.value.size
    }

    override suspend fun clearChatHistory() {
        _messages.value = emptyList()
    }

    override suspend fun deleteMessagesOlderThan(days: Int): Boolean { return false }

    override suspend fun getMessageCount(): Int = _messages.value.size
}

class FakePersonalityRepository : PersonalityRepository {
    private val default = Personality(
        id = "default", 
        name = "Assistant", 
        description = "Test personality",
        category = com.platinumassistant.domain.entities.PersonalityCategory.CUSTOM,
        voiceId = "default-voice"
    )
    
    override fun getAllPersonalities(): Flow<List<Personality>> = flow { emit(listOf(default)) }
    override fun getPersonalityById(id: String): Flow<Personality?> = flow { emit(if (id == "default") default else null) }
    override fun getPersonalitiesByCategory(category: String): Flow<List<Personality>> = flow { emit(listOf(default)) }
    override fun getFavoritePersonalities(): Flow<List<Personality>> = flow { emit(emptyList()) }
    override suspend fun addPersonality(personality: Personality) {}
    override suspend fun updatePersonality(personality: Personality) {}
    override suspend fun deletePersonality(id: String) {}
    override suspend fun toggleFavorite(id: String): Boolean = false
    override suspend fun selectPersonality(id: String): Boolean = id == "default"
    override suspend fun getSelectedPersonality(): Personality? = default
    override suspend fun updateUsageStatistics(id: String) {}
    override suspend fun searchPersonalities(query: String): List<Personality> = listOf(default)
    override fun getTrendingPersonalities(limit: Int): Flow<List<Personality>> = flow { emit(listOf(default)) }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    @Test
    fun `sendMessage adds user and assistant messages`() = runTest {
        val msgRepo = FakeMessageRepository()
        val personalityRepo = FakePersonalityRepository()
        val personalityUseCases = PersonalityUseCases(
            GetAllPersonalitiesUseCase(personalityRepo),
            GetPersonalitiesByCategoryUseCase(personalityRepo),
            GetFavoritePersonalitiesUseCase(personalityRepo),
            SelectPersonalityUseCase(personalityRepo),
            ToggleFavoritePersonalityUseCase(personalityRepo),
            GetTrendingPersonalitiesUseCase(personalityRepo)
        )

        val vm = ChatViewModel(msgRepo, personalityRepo, personalityUseCases)

        vm.sendMessage("Hello")

        // allow coroutines to run
        kotlinx.coroutines.delay(200)

        val count = msgRepo.getMessageCount()
        assertEquals(2, count) // user + assistant
    }
}
