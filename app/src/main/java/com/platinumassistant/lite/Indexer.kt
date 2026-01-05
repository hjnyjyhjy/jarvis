package com.platinumassistant.lite

import android.content.Context
import java.io.File

/**
 * Minimal newline-delimited index. Each line: key|value
 * Lightweight, safe for low-end devices. Guarded by FEATURE_INDEXING.
 */
object Indexer {
    private const val FILENAME = "jl_index.ndjson"

    private fun file(ctx: Context): File = File(ctx.filesDir, FILENAME)

    fun addEntry(ctx: Context, key: String, value: String) {
        if (!FeatureFlags.FEATURE_INDEXING) return
        try {
            file(ctx).appendText("${key.trim()}|${value.trim()}\n")
        } catch (_: Exception) {
        }
    }

    fun searchPrefix(ctx: Context, prefix: String, max: Int = 20): List<Pair<String, String>> {
        if (!FeatureFlags.FEATURE_INDEXING) return emptyList()
        val out = mutableListOf<Pair<String, String>>()
        val p = prefix.lowercase()
        try {
            file(ctx).bufferedReader().useLines { lines ->
                for (ln in lines) {
                    val parts = ln.split("|", limit = 2)
                    if (parts.size < 2) continue
                    val k = parts[0]
                    val v = parts[1]
                    if (k.lowercase().startsWith(p)) {
                        out.add(Pair(k, v))
                        if (out.size >= max) break
                    }
                }
            }
        } catch (_: Exception) {
        }
        return out
    }

    fun listAll(ctx: Context): List<Pair<String, String>> {
        if (!FeatureFlags.FEATURE_INDEXING) return emptyList()
        val out = mutableListOf<Pair<String, String>>()
        try {
            file(ctx).forEachLine { ln ->
                val parts = ln.split("|", limit = 2)
                if (parts.size == 2) out.add(Pair(parts[0], parts[1]))
            }
        } catch (_: Exception) {
        }
        return out
    }

    fun clear(ctx: Context) {
        try { file(ctx).delete() } catch (_: Exception) {}
    }
}
