package com.platinumassistant.lite

/**
 * Tiny feature flags. Keep defaults conservative for low-end devices.
 * Change flags here before enabling heavy features.
 */
object FeatureFlags {
    // If true, assistant will auto-generate next files (USE WITH CAUTION)
    const val AUTORUN = true

    // Heavy features - OFF by default
    const val FEATURE_AI = false
    const val FEATURE_SPEAKER_ID = false
    const val FEATURE_WAKEWORD = false
    const val FEATURE_CONTINUOUS = false

    // Light features - safe to enable
    const val FEATURE_MEMORY = true
    const val FEATURE_INDEXING = true
}
