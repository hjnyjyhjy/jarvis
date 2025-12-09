package com.platinumassistant.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room database for Platinum Assistant
 * Currently a placeholder - will be expanded with entities
 */
@Database(
    entities = [],  // TODO: Add entities
    version = 1,
    exportSchema = false
)
abstract class PlatinumDatabase : RoomDatabase() {
    // TODO: Add DAOs
}
