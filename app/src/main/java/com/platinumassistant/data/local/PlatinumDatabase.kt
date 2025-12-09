package com.platinumassistant.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

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
        // TODO: Add Room entities (tables)
        // MessageEntity::class,
        // TaskEntity::class,
        // PersonalityEntity::class,
        // PreferenceEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    // TODO: Add TypeConverters for complex types
    // DateConverters::class,
    // ListConverters::class,
)
abstract class PlatinumDatabase : RoomDatabase() {
    
    // TODO: Add DAO (Data Access Object) abstract functions
    // abstract fun messageDao(): MessageDao
    // abstract fun taskDao(): TaskDao
    // abstract fun personalityDao(): PersonalityDao
    
    /**
     * Database migration strategy:
     * - Use fallbackToDestructiveMigration() for dev builds
     * - Implement proper migrations for release builds
     */
}
