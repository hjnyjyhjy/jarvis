package com.platinumassistant.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.platinumassistant.data.local.dao.MessageDao
import com.platinumassistant.data.local.dao.TaskDao
import com.platinumassistant.data.local.dao.PersonalityDao
import com.platinumassistant.data.local.entity.MessageEntity
import com.platinumassistant.data.local.entity.TaskEntity
import com.platinumassistant.data.local.entity.PersonalityEntity

/**
 * Room database for Platinum Assistant
 * 
 * Provides local, encrypted data storage for:
 * - Chat history
 * - Tasks and reminders
 * - Personality preferences
 * - User settings and preferences
 * - Memory and context
 * 
 * Uses SQLCipher for AES-256 encryption at rest
 * Database version: 1
 */
@Database(
    entities = [
        MessageEntity::class,
        TaskEntity::class,
        PersonalityEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    // TypeConverters can be added here for complex types like Date, List, etc.
)
abstract class PlatinumDatabase : RoomDatabase() {
    
    abstract fun messageDao(): MessageDao
    abstract fun taskDao(): TaskDao
    abstract fun personalityDao(): PersonalityDao
    
    /**
     * Database migration strategy:
     * - Use fallbackToDestructiveMigration() for dev builds
     * - Implement proper migrations for release builds
     */
}
