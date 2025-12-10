package com.platinumassistant.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import androidx.room.Room
import com.platinumassistant.core.security.EncryptionManager
import com.platinumassistant.data.local.PlatinumDatabase
import com.platinumassistant.data.repositories.MessageRepositoryImpl
import com.platinumassistant.data.repositories.PersonalityRepositoryImpl
import com.platinumassistant.data.repositories.TaskRepositoryImpl
import com.platinumassistant.domain.repositories.MessageRepository
import com.platinumassistant.domain.repositories.PersonalityRepository
import com.platinumassistant.domain.repositories.TaskRepository
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
        return PreferenceDataStoreFactory.create(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(PREFERENCES_NAME) }
        )
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

    // Repository Providers

    @Provides
    @Singleton
    fun provideMessageRepository(
        database: PlatinumDatabase
    ): MessageRepository {
        return MessageRepositoryImpl(database.messageDao())
    }

    @Provides
    @Singleton
    fun providePersonalityRepository(
        database: PlatinumDatabase
    ): PersonalityRepository {
        return PersonalityRepositoryImpl(database.personalityDao())
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        database: PlatinumDatabase
    ): TaskRepository {
        return TaskRepositoryImpl(database.taskDao())
    }
}
