package com.platinumassistant.lite

// Tiny Speaker ID stub. No heavy work locally.
object SpeakerId {
    fun isEnabled(): Boolean = FeatureFlags.FEATURE_SPEAKER_ID

    // Local placeholder - returns human-readable status
    fun status(): String = if (isEnabled()) "enabled (cloud required)" else "disabled"

    // When enabled, identification must be performed in cloud (not locally).
    // This method is a local marker that a sample should be uploaded by the app or CI.
    fun markForCloudIdentification(samplePath: String): Boolean {
        if (!isEnabled()) return false
        // Intentionally NO upload here on device. Return true to indicate the sample is ready.
        return true
    }
}
