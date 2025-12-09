package com.platinumassistant.core.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager for encrypted data storage
 */
@Singleton
class EncryptionManager @Inject constructor(
    private val context: Context
) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        context,
        "platinum_encrypted_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveSecureString(key: String, value: String) {
        encryptedSharedPreferences.edit().putString(key, value).apply()
    }

    fun getSecureString(key: String, defaultValue: String = ""): String {
        return encryptedSharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun deleteSecureString(key: String) {
        encryptedSharedPreferences.edit().remove(key).apply()
    }

    fun clearAllSecureData() {
        encryptedSharedPreferences.edit().clear().apply()
    }
}
