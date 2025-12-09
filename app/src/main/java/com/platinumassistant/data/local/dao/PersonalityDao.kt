package com.platinumassistant.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.platinumassistant.data.local.entity.PersonalityEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Personality entities
 * Provides database operations for personality management and user preferences
 */
@Dao
interface PersonalityDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonality(personality: PersonalityEntity)
    
    @Update
    suspend fun updatePersonality(personality: PersonalityEntity)
    
    @Delete
    suspend fun deletePersonality(personality: PersonalityEntity)
    
    @Query("SELECT * FROM personalities ORDER BY name ASC")
    fun getAllPersonalities(): Flow<List<PersonalityEntity>>
    
    @Query("SELECT * FROM personalities WHERE id = :id")
    suspend fun getPersonalityById(id: String): PersonalityEntity?
    
    @Query("SELECT * FROM personalities WHERE category = :category ORDER BY name ASC")
    fun getPersonalitiesByCategory(category: String): Flow<List<PersonalityEntity>>
    
    @Query("SELECT * FROM personalities WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoritePersonalities(): Flow<List<PersonalityEntity>>
    
    @Query("SELECT * FROM personalities WHERE isSelected = 1 LIMIT 1")
    suspend fun getSelectedPersonality(): PersonalityEntity?
    
    @Query("SELECT * FROM personalities ORDER BY usageCount DESC LIMIT :limit")
    fun getTrendingPersonalities(limit: Int = 10): Flow<List<PersonalityEntity>>
    
    @Query("SELECT * FROM personalities WHERE language = :language ORDER BY name ASC")
    fun getPersonalitiesByLanguage(language: String): Flow<List<PersonalityEntity>>
    
    @Query("UPDATE personalities SET isFavorite = 0")
    suspend fun clearAllFavorites()
    
    @Query("UPDATE personalities SET isSelected = 0")
    suspend fun clearSelectedPersonality()
    
    @Query("UPDATE personalities SET usageCount = usageCount + 1 WHERE id = :id")
    suspend fun incrementUsageCount(id: String)
    
    @Query("SELECT COUNT(*) FROM personalities WHERE isFavorite = 1")
    suspend fun getFavoriteCount(): Int
    
    @Query("DELETE FROM personalities")
    suspend fun clearAllPersonalities()
}
