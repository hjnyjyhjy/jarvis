package com.platinumassistant.core.wake

/**
 * Interface for a wake-word detector implementation.
 * Implementations can wrap Porcupine, Precise, or any local detector.
 */
interface WakeWordDetector {
    /** Initialize detector with model path or params. Call off main thread. */
    suspend fun initialize(modelPath: String?)

    /** Start listening for the wake-word. Calls `onDetected` when triggered. */
    fun startListening(onDetected: () -> Unit, onError: (Throwable) -> Unit)

    /** Stop listening and release resources. */
    fun stopListening()

    fun isReady(): Boolean
}
