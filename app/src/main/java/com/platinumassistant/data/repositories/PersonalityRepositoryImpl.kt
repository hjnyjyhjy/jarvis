package com.platinumassistant.data.repositories

import com.platinumassistant.data.local.dao.PersonalityDao
import com.platinumassistant.data.local.entity.PersonalityEntity
import com.platinumassistant.domain.entities.Personality
import com.platinumassistant.domain.repositories.PersonalityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of PersonalityRepository
 * Handles all personality-related data operations
 */
class PersonalityRepositoryImpl @Inject constructor(
    private val personalityDao: PersonalityDao
) : PersonalityRepository {
    
    override fun getAllPersonalities(): Flow<List<Personality>> =
        personalityDao.getAllPersonalities().map { entities ->
            entities.map { it.toDomainModel() }
        }
    
    override fun getPersonalityById(id: String): Flow<Personality?> =
        personalityDao.getPersonalitiesByCategory("").map { _ ->
            personalityDao.getPersonalityById(id)?.toDomainModel()
        }
    
    override fun getPersonalitiesByCategory(category: String): Flow<List<Personality>> =
        personalityDao.getPersonalitiesByCategory(category).map { entities ->
            entities.map { it.toDomainModel() }
        }
    
    override fun getFavoritePersonalities(): Flow<List<Personality>> =
        personalityDao.getFavoritePersonalities().map { entities ->
            entities.map { it.toDomainModel() }
        }
    
    override suspend fun getSelectedPersonality(): Personality? =
        personalityDao.getSelectedPersonality()?.toDomainModel()
    
    override fun getTrendingPersonalities(limit: Int): Flow<List<Personality>> =
        personalityDao.getTrendingPersonalities(limit).map { entities ->
            entities.map { it.toDomainModel() }
        }
    
    override suspend fun addPersonality(personality: Personality) {
        personalityDao.insertPersonality(PersonalityEntity.fromDomainModel(personality))
    }
    
    override suspend fun updatePersonality(personality: Personality) {
        personalityDao.updatePersonality(PersonalityEntity.fromDomainModel(personality))
    }
    
    override suspend fun selectPersonality(id: String): Boolean = try {
        // Clear previous selection
        personalityDao.clearSelectedPersonality()
        
        // Get the personality and mark it as selected
        val personality = personalityDao.getPersonalityById(id)
        if (personality != null) {
            val updated = personality.copy(isSelected = true)
            personalityDao.updatePersonality(updated)
            personalityDao.incrementUsageCount(id)
            true
        } else {
            false
        }
    } catch (e: Exception) {
        false
    }
    
    override suspend fun toggleFavorite(id: String): Boolean = try {
        val personality = personalityDao.getPersonalityById(id)
        if (personality != null) {
            val updated = personality.copy(isFavorite = !personality.isFavorite)
            personalityDao.updatePersonality(updated)
            true
        } else {
            false
        }
    } catch (e: Exception) {
        false
    }
    
    override suspend fun deletePersonality(id: String): Boolean = try {
        val personality = personalityDao.getPersonalityById(id)
        if (personality != null) {
            personalityDao.deletePersonality(personality)
            true
        } else {
            false
        }
    } catch (e: Exception) {
        false
    }
    
    override fun getPersonalitiesByLanguage(language: String): Flow<List<Personality>> =
        personalityDao.getPersonalitiesByLanguage(language).map { entities ->
            entities.map { it.toDomainModel() }
        }
    
    override suspend fun clearAllPersonalities() {
        personalityDao.clearAllPersonalities()
    }
}
