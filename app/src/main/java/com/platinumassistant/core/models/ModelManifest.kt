package com.platinumassistant.core.models

data class ModelManifest(
    val models: List<ModelEntry>
)

data class ModelEntry(
    val id: String,
    val filename: String,
    val url: String,
    val sha256: String?,
    val size_bytes: Long?
)
