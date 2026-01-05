package com.platinumassistant.core.ai

/**
 * Interface for a local speech recognizer (STT).
 * Implementations can wrap Vosk, Whisper (via JNI), or any local engine.
 */
interface SpeechRecognizer {
    /**
     * Initialize models and resources. Should be called off the main thread.
     */
    suspend fun initialize(modelPath: String)

    /**
     * Start listening / recognize continuously or for a single utterance depending on implementation.
     */
    fun startListening(onResult: (String) -> Unit, onError: (Throwable) -> Unit)

    /**
     * Stop listening and release resources if needed.
     */
    fun stopListening()

    /**
     * Check whether recognizer is ready
     */
    fun isReady(): Boolean
}
