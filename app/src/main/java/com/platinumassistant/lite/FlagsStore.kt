package com.platinumassistant.lite

import android.content.Context

/**
 * Minimal SharedPreferences wrapper for feature flags.
 * Keeps implementation tiny and avoids heavy deps.
 */
object FlagsStore {
    private const val PREFS = "jl_prefs"

    fun getFlag(ctx: Context, key: String, default: Boolean = false): Boolean {
        val sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return sp.getBoolean(key, default)
    }

    fun setFlag(ctx: Context, key: String, value: Boolean) {
        val sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        sp.edit().putBoolean(key, value).apply()
    }
}
