package com.platinumassistant.domain.usecases

import com.platinumassistant.domain.entities.Personality
import com.platinumassistant.domain.repositories.PersonalityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all personalities
 */
class GetAllPersonalitiesUseCase @Inject constructor(
    private val personalityRepository: PersonalityRepository
) {
    operator fun invoke(): Flow<List<Personality>> {
        return personalityRepository.getAllPersonalities()
    }
}

/**
 * Use case for getting favorite personalities
 */
class GetFavoritePersonalitiesUseCase @Inject constructor(
    private val personalityRepository: PersonalityRepository
) {
    operator fun invoke(): Flow<List<Personality>> {
        return personalityRepository.getFavoritePersonalities()
    }
}

/**
 * Use case for selecting a personality
 */
class SelectPersonalityUseCase @Inject constructor(
    private val personalityRepository: PersonalityRepository
) {
    suspend operator fun invoke(id: String) {
        personalityRepository.updateUsageStatistics(id)
    }
}
