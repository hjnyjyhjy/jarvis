package com.platinumassistant.domain.usecases

import com.platinumassistant.domain.entities.Personality
import com.platinumassistant.domain.entities.PersonalityCategory
import com.platinumassistant.domain.repositories.PersonalityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Container class for all personality-related use cases
 * Simplifies injection and grouping of related use cases
 */
@Inject
class PersonalityUseCases(
    val getAllPersonalities: GetAllPersonalitiesUseCase,
    val getByCategory: GetPersonalitiesByCategoryUseCase,
    val getFavorites: GetFavoritePersonalitiesUseCase,
    val selectPersonality: SelectPersonalityUseCase,
    val toggleFavorite: ToggleFavoritePersonalityUseCase,
    val getTrendingPersonalities: GetTrendingPersonalitiesUseCase
)

/**
 * Use case for retrieving all personalities with optional filtering
 */
class GetAllPersonalitiesUseCase @Inject constructor(
    private val personalityRepository: PersonalityRepository
) {
    operator fun invoke(): Flow<List<Personality>> {
        return personalityRepository.getAllPersonalities()
    }
}

/**
 * Use case for getting personalities by category
 */
class GetPersonalitiesByCategoryUseCase @Inject constructor(
    private val personalityRepository: PersonalityRepository
) {
    operator fun invoke(category: PersonalityCategory): Flow<List<Personality>> {
        return personalityRepository.getPersonalitiesByCategory(category.name)
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
 * Use case for selecting a personality and updating stats
 */
class SelectPersonalityUseCase @Inject constructor(
    private val personalityRepository: PersonalityRepository
) {
    operator fun invoke(id: String): Flow<Boolean> {
        return flowOf(personalityRepository.selectPersonality(id))
    }
}

/**
 * Use case for toggling personality favorite status
 */
class ToggleFavoritePersonalityUseCase @Inject constructor(
    private val personalityRepository: PersonalityRepository
) {
    operator fun invoke(id: String): Flow<Boolean> {
        return flowOf(personalityRepository.toggleFavorite(id))
    }
}

/**
 * Use case for getting trending personalities by usage
 */
class GetTrendingPersonalitiesUseCase @Inject constructor(
    private val personalityRepository: PersonalityRepository
) {
    operator fun invoke(limit: Int = 5): Flow<List<Personality>> {
        return personalityRepository.getTrendingPersonalities(limit)
    }
}
