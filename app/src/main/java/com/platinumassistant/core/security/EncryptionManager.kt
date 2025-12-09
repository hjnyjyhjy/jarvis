package com.platinumassistant.core.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager for encrypted data storage using AES-256 encryption
 * All sensitive data is encrypted at rest using Android SecurityCrypto library
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

    /**
     * Save an encrypted string value
     */
    fun saveSecureString(key: String, value: String) {
        encryptedSharedPreferences.edit().putString(key, value).apply()
    }

    /**
     * Retrieve an encrypted string value
     */
    fun getSecureString(key: String, defaultValue: String = ""): String {
        return encryptedSharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    /**
     * Save encrypted boolean
     */
    fun saveSecureBoolean(key: String, value: Boolean) {
        encryptedSharedPreferences.edit().putBoolean(key, value).apply()
    }

    /**
     * Retrieve encrypted boolean
     */
    fun getSecureBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return encryptedSharedPreferences.getBoolean(key, defaultValue)
    }

    /**
     * Delete a specific encrypted value
     */
    fun deleteSecureString(key: String) {
        encryptedSharedPreferences.edit().remove(key).apply()
    }

    /**
     * Clear all encrypted data
     */
    fun clearAllSecureData() {
        encryptedSharedPreferences.edit().clear().apply()
    }

    /**
     * Generate a secure random token
     */
    fun generateSecureToken(length: Int = 32): String {
        val secureRandom = SecureRandom()
        val values = ByteArray(length)
        secureRandom.nextBytes(values)
        return values.joinToString("") { "%02x".format(it) }
    }

    /**
     * Check if a specific key exists in encrypted storage
     */
    fun hasKey(key: String): Boolean {
        return encryptedSharedPreferences.contains(key)
    }
}
