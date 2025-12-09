package com.platinumassistant.domain.repositories

import com.platinumassistant.domain.entities.Personality
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for personality data access
 */
interface PersonalityRepository {
    
    fun getAllPersonalities(): Flow<List<Personality>>
    
    fun getPersonalityById(id: String): Flow<Personality?>
    
    fun getFavoritePersonalities(): Flow<List<Personality>>
    
    suspend fun addPersonality(personality: Personality)
    
    suspend fun updatePersonality(personality: Personality)
    
    suspend fun deletePersonality(id: String)
    
    suspend fun toggleFavorite(id: String, isFavorite: Boolean)
    
    suspend fun updateUsageStatistics(id: String)
}
