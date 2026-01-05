package com.platinumassistant.core.ai

/**
 * Interface for a local NLU component: intent/entity extraction and contextual understanding.
 */
interface NLUProcessor {
    /**
     * Initialize NLU model/resources (BERT, tinyBERT, or custom classifier).
     */
    suspend fun initialize(modelPath: String)

    /**
     * Process a text utterance and return a simple result object. Implementations should
     * provide context handling across multiple calls if needed.
     */
    fun process(text: String): NLUResult

    fun isReady(): Boolean
}

data class NLUResult(
    val intent: String,
    val confidence: Float,
    val entities: Map<String, String> = emptyMap(),
    val followUpRequired: Boolean = false
)
