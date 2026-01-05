package com.platinumassistant.core.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("platinum_prefs")

object PreferencesManager {
    private val WAKE_WORD_KEY = booleanPreferencesKey("wake_word_enabled")

    private val ONLINE_MODE_KEY = booleanPreferencesKey("online_mode_enabled")
    private val ONLINE_NLU_URL = preferencesKey<String>("online_nlu_url")
    private val ONLINE_ASR_URL = preferencesKey<String>("online_asr_url")
    private val TTS_ENGINE = preferencesKey<String>("tts_engine")
    private val POWER_MODE = preferencesKey<String>("power_mode")
    private val LANGUAGE = preferencesKey<String>("language")

    fun isWakeWordEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[WAKE_WORD_KEY] ?: false
        }
    }

    suspend fun setWakeWordEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[WAKE_WORD_KEY] = enabled
        }
    }

    fun isOnlineModeEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[ONLINE_MODE_KEY] ?: false
        }
    }

    suspend fun setOnlineModeEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ONLINE_MODE_KEY] = enabled
        }
    }

    fun getOnlineNluUrl(context: Context): Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[ONLINE_NLU_URL]
    }

    suspend fun setOnlineNluUrl(context: Context, url: String?) {
        context.dataStore.edit { prefs ->
            if (url == null) prefs.remove(ONLINE_NLU_URL) else prefs[ONLINE_NLU_URL] = url
        }
    }

    fun getOnlineAsrUrl(context: Context): Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[ONLINE_ASR_URL]
    }

    suspend fun setOnlineAsrUrl(context: Context, url: String?) {
        context.dataStore.edit { prefs ->
            if (url == null) prefs.remove(ONLINE_ASR_URL) else prefs[ONLINE_ASR_URL] = url
        }
    }

    // TTS engine: "android" (default) or "coqui"
    fun getTtsEngine(context: Context): Flow<String> = context.dataStore.data.map { prefs ->
        prefs[TTS_ENGINE] ?: "android"
    }

    suspend fun setTtsEngine(context: Context, engine: String) {
        context.dataStore.edit { prefs ->
            prefs[TTS_ENGINE] = engine
        }
    }

    // Power mode: "performance", "balanced", "battery_saver"
    fun getPowerMode(context: Context): Flow<String> = context.dataStore.data.map { prefs ->
        prefs[POWER_MODE] ?: "balanced"
    }

    suspend fun setPowerMode(context: Context, mode: String) {
        context.dataStore.edit { prefs ->
            prefs[POWER_MODE] = mode
        }
    }

    // Language preference: "auto", "ar", "en"
    fun getLanguage(context: Context): Flow<String> = context.dataStore.data.map { prefs ->
        prefs[LANGUAGE] ?: "auto"
    }

    suspend fun setLanguage(context: Context, lang: String) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE] = lang
        }
    }
}
