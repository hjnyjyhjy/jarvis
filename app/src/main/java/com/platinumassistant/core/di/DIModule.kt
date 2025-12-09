package com.platinumassistant.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.platinumassistant.core.security.EncryptionManager
import com.platinumassistant.data.local.PlatinumDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val PREFERENCES_NAME = "platinum_preferences"
private const val DATABASE_NAME = "platinum_database"

/**
 * Dependency Injection module for providing singleton instances
 * Manages all global dependencies with Hilt/Dagger
 */
@Module
@InstallIn(SingletonComponent::class)
object DIModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.preferencesDataStore(PREFERENCES_NAME)
    }

    @Provides
    @Singleton
    fun providePlatinumDatabase(
        @ApplicationContext context: Context
    ): PlatinumDatabase {
        return Room.databaseBuilder(
            context,
            PlatinumDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideEncryptionManager(
        @ApplicationContext context: Context
    ): EncryptionManager {
        return EncryptionManager(context)
    }
}
