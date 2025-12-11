package com.platinumassistant.domain.repositories

import com.platinumassistant.domain.entities.Personality
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for personality data access and persistence
 */
interface PersonalityRepository {
    
    fun getAllPersonalities(): Flow<List<Personality>>
    
    fun getPersonalityById(id: String): Flow<Personality?>

    // Category is stored as a string in the DAO; use string names here for compatibility
    fun getPersonalitiesByCategory(category: String): Flow<List<Personality>>
    
    fun getFavoritePersonalities(): Flow<List<Personality>>
    
    fun getTrendingPersonalities(limit: Int): Flow<List<Personality>>
    
    suspend fun addPersonality(personality: Personality)
    
    suspend fun updatePersonality(personality: Personality)
    
    suspend fun deletePersonality(id: String)

    suspend fun toggleFavorite(id: String): Boolean

    suspend fun selectPersonality(id: String): Boolean

    suspend fun getSelectedPersonality(): Personality?

    suspend fun updateUsageStatistics(id: String)

    suspend fun searchPersonalities(query: String): List<Personality>
}
