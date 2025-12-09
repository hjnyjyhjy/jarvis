package com.platinumassistant.domain.usecases

import com.platinumassistant.domain.entities.Personality
import com.platinumassistant.domain.entities.PersonalityCategory
import com.platinumassistant.domain.repositories.PersonalityRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PersonalityUseCasesTest {

    private lateinit var personalityRepository: PersonalityRepository
    private lateinit var getAllPersonalitiesUseCase: GetAllPersonalitiesUseCase
    private lateinit var selectPersonalityUseCase: SelectPersonalityUseCase

    @Before
    fun setUp() {
        personalityRepository = mockk()
        getAllPersonalitiesUseCase = GetAllPersonalitiesUseCase(personalityRepository)
        selectPersonalityUseCase = SelectPersonalityUseCase(personalityRepository)
    }

    @Test
    fun getAllPersonalities_ReturnsPersonalityList() = runTest {
        // Arrange
        val mockPersonalities = listOf(
            Personality(
                id = "1",
                name = "Jarvis",
                description = "Technical assistant",
                category = PersonalityCategory.TECHNICAL,
                voiceId = "jarvis_voice"
            )
        )
        coEvery { personalityRepository.getAllPersonalities() } returns flowOf(mockPersonalities)

        // Act & Assert
        getAllPersonalitiesUseCase().collect { personalities ->
            assert(personalities.size == 1)
            assert(personalities[0].name == "Jarvis")
        }
    }

    @Test
    fun selectPersonality_UpdatesUsageStatistics() = runTest {
        // Arrange
        coEvery { personalityRepository.updateUsageStatistics(any()) } returns Unit

        // Act
        selectPersonalityUseCase("1")

        // Assert
        io.mockk.verify { runBlocking { personalityRepository.updateUsageStatistics("1") } }
    }
}
