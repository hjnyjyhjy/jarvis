package com.platinumassistant.lite

import android.content.Context

/**
 * Tiny memory helper. Uses SharedPreferences for small facts.
 */
object MemoryManager {
    private const val PREFS = "jl_memory"

    fun put(ctx: Context, key: String, value: String) {
        if (!FeatureFlags.FEATURE_MEMORY) return
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(key, value).apply()
    }

    fun get(ctx: Context, key: String, default: String = ""): String {
        if (!FeatureFlags.FEATURE_MEMORY) return default
        return ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(key, default) ?: default
    }

    fun remove(ctx: Context, key: String) {
        if (!FeatureFlags.FEATURE_MEMORY) return
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().remove(key).apply()
    }

    fun listKeys(ctx: Context): Set<String> {
        if (!FeatureFlags.FEATURE_MEMORY) return emptySet()
        return ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).all.keys
    }
}
