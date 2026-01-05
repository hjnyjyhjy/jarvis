package com.platinumassistant.core.ai

/**
 * Interface for local TTS engines. Implementations can wrap Coqui TTS, PicoTTS, or Android TTS.
 */
interface SpeechSynthesizer {
    /**
     * Initialize the TTS model/resources.
     */
    suspend fun initialize(modelPath: String)

    /**
     * Synthesize text to audio and play it; return when playback complete or error occurred.
     */
    fun speak(text: String, onComplete: (() -> Unit)? = null)

    /**
     * Stop playback immediately.
     */
    fun stop()

    /**
     * True if initialized and ready
     */
    fun isReady(): Boolean
}
