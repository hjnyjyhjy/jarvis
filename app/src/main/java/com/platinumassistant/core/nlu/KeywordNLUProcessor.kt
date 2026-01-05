package com.platinumassistant.core.nlu

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.platinumassistant.core.ai.NLUProcessor
import com.platinumassistant.core.ai.NLUResult
import timber.log.Timber

data class NluManifest(@SerializedName("nlu") val nlu: Map<String, List<NluEntry>>)
data class NluEntry(val intent: String, val examples: List<String>, val response: String?)

/**
 * Very small offline NLU that matches keywords from examples (case-insensitive).
 * Not a full NLU — but works for basic intents and is lightweight (no heavy models).
 */
class KeywordNLUProcessor(private val context: Context) : NLUProcessor {
    private var manifest: NluManifest? = null
    private var lang: String = "en"

    override suspend fun initialize(modelPath: String) {
        // modelPath unused (we use assets manifest); read the manifest
        try {
            context.assets.open("nlu_manifest.json").use { stream ->
                val reader = stream.reader()
                manifest = Gson().fromJson(reader, NluManifest::class.java)
            }
        } catch (t: Throwable) {
            Timber.e(t, "Failed to read NLU manifest")
            manifest = null
        }
    }

    fun setLanguage(language: String) {
        lang = when (language) {
            "ar" -> "ar"
            "en" -> "en"
            else -> "en"
        }
    }

    override fun process(text: String): NLUResult {
        val entries = manifest?.nlu?.get(lang) ?: emptyList()
        val lowered = text.lowercase()
        for (entry in entries) {
            for (ex in entry.examples) {
                if (lowered.contains(ex.lowercase())) {
                    return NLUResult(intent = entry.intent, confidence = 0.9f, entities = emptyMap(), followUpRequired = false)
                }
            }
        }
        return NLUResult(intent = "unknown", confidence = 0f)
    }

    override fun isReady(): Boolean = manifest != null
}
