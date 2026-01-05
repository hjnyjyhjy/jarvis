package com.platinumassistant.core.models

import android.content.Context
import com.google.gson.Gson
import java.io.InputStreamReader

/**
 * Simple repository that reads `assets/models_manifest.json` and returns a list of models.
 */
object ModelRepository {
    fun loadManifest(context: Context): List<ModelEntry> {
        return try {
            context.assets.open("models_manifest.json").use { stream ->
                val reader = InputStreamReader(stream)
                val manifest = Gson().fromJson(reader, ModelManifest::class.java)
                manifest.models
            }
        } catch (t: Throwable) {
            emptyList()
        }
    }
}
