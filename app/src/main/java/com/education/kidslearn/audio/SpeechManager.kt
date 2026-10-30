package com.education.kidslearn.audio

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import java.util.Locale

/**
 * Owns Android Text-to-Speech and reports spelling progress
 * from actual utterance callbacks.
 */
data class SpeechVoice(
    val id: String,
    val label: String
)

class SpeechManager(context: Context) {

    private var textToSpeech: TextToSpeech? = null
    private var speechReady = false
    private var pendingSpeechAction: (() -> Unit)? = null
    private var spellingProgressCallback: ((Int) -> Unit)? = null
    private var voicesChangedCallback: ((List<SpeechVoice>) -> Unit)? = null
    private var spellingSession = 0L

    private val speechLocale = Locale.US
    private val mainHandler = Handler(Looper.getMainLooper())

    private var speechVolume = 1.0f
    private var speechRate = 0.85f
    private var selectedVoiceId: String? = null

    init {
        textToSpeech = TextToSpeech(context.applicationContext) { status ->
            val tts = textToSpeech

            if (status == TextToSpeech.SUCCESS && tts != null) {
                val result = tts.setLanguage(speechLocale)

                speechReady = result != TextToSpeech.LANG_MISSING_DATA &&
                        result != TextToSpeech.LANG_NOT_SUPPORTED

                if (speechReady) {
                    applySpeechSettings()

                    tts.setOnUtteranceProgressListener(
                        object : UtteranceProgressListener() {

                            override fun onStart(utteranceId: String?) {
                                val parts = utteranceId?.split(":")
                                    ?: return

                                if (
                                    parts.size == 3 &&
                                    parts[0] == "spell" &&
                                    parts[1].toLongOrNull() == spellingSession
                                ) {
                                    val index = parts[2].toIntOrNull()
                                        ?: return

                                    mainHandler.post {
                                        spellingProgressCallback?.invoke(index)
                                    }
                                }
                            }

                            override fun onDone(utteranceId: String?) {
                                val parts = utteranceId?.split(":")
                                    ?: return

                                if (
                                    parts.size >= 2 &&
                                    parts[0] == "spellDone" &&
                                    parts[1].toLongOrNull() == spellingSession
                                ) {
                                    mainHandler.post {
                                        spellingProgressCallback?.invoke(-1)
                                    }
                                }
                            }

                            @Deprecated("Deprecated in Java")
                            override fun onError(utteranceId: String?) {
                                mainHandler.post {
                                    spellingProgressCallback?.invoke(-1)
                                }
                            }
                        }
                    )

                    voicesChangedCallback?.invoke(getAvailableVoices())

                    val action = pendingSpeechAction
                    pendingSpeechAction = null
                    action?.invoke()
                } else {
                    pendingSpeechAction = null
                }
            } else {
                pendingSpeechAction = null
            }
        }
    }

    private fun applySpeechSettings() {
        val tts = textToSpeech ?: return

        tts.setSpeechRate(speechRate.coerceIn(0.5f, 2.0f))

        val voice = selectedVoiceId
            ?.let { id ->
                tts.voices?.firstOrNull { it.name == id }
            }

        if (voice != null && voice.locale.language == "en") {
            tts.voice = voice
        } else {
            tts.setLanguage(speechLocale)
        }
    }

    private fun speechParameters(): Bundle {
        return Bundle().apply {
            putFloat(
                TextToSpeech.Engine.KEY_PARAM_VOLUME,
                speechVolume.coerceIn(0.0f, 1.0f)
            )
        }
    }

    fun setSpellingProgressCallback(callback: ((Int) -> Unit)?) {
        spellingProgressCallback = callback
    }

    /**
     * Notifies the caller when the TTS engine has initialized
     * and its available English voices can be queried.
     */
    fun setVoicesChangedCallback(
        callback: ((List<SpeechVoice>) -> Unit)?
    ) {
        voicesChangedCallback = callback

        if (speechReady) {
            callback?.invoke(getAvailableVoices())
        }
    }

    /**
     * Returns English voices reported by the installed TTS engine.
     * US English voices are listed first when available.
     */
    fun getAvailableVoices(): List<SpeechVoice> {
        val tts = textToSpeech ?: return emptyList()
        if (!speechReady) return emptyList()

        return tts.voices
            .orEmpty()
            .filter { it.locale.language == "en" }
            .distinctBy { it.name }
            .sortedWith(
                compareByDescending<Voice> {
                    it.locale.country == "US"
                }.thenBy {
                    it.name
                }
            )
            .map { voice ->
                val localeLabel = voice.locale.displayName
                val qualityLabel = when (voice.quality) {
                    Voice.QUALITY_VERY_HIGH -> "Very high quality"
                    Voice.QUALITY_HIGH -> "High quality"
                    Voice.QUALITY_NORMAL -> "Standard quality"
                    else -> "Voice"
                }

                SpeechVoice(
                    id = voice.name,
                    label = "${voice.name} — $localeLabel ($qualityLabel)"
                )
            }
    }

    /**
     * Sets speech volume from 0.0 (silent) to 1.0 (full TTS volume).
     */
    fun setSpeechVolume(volume: Float) {
        speechVolume = volume.coerceIn(0.0f, 1.0f)
    }

    /**
     * Sets the speech rate. A rate of 1.0 is the engine's normal rate.
     */
    fun setSpeechSpeed(rate: Float) {
        speechRate = rate.coerceIn(0.5f, 2.0f)
        if (speechReady) {
            textToSpeech?.setSpeechRate(speechRate)
        }
    }

    /**
     * Selects an installed English voice by its engine-provided name.
     * Pass null to return to the default US English voice.
     */
    fun setVoice(voiceId: String?) {
        val tts = textToSpeech

        if (voiceId == null) {
            selectedVoiceId = null
            if (speechReady) {
                tts?.setLanguage(speechLocale)
            }
            return
        }

        val voice = tts?.voices
            ?.firstOrNull { it.name == voiceId }

        if (voice != null && voice.locale.language == "en") {
            selectedVoiceId = voice.name

            if (speechReady) {
                tts.voice = voice
            }
        }
    }

    fun speakWord(word: String) {
        if (word.isBlank()) return

        val tts = textToSpeech

        if (!speechReady || tts == null) {
            pendingSpeechAction = { speakWord(word) }
            return
        }

        pendingSpeechAction = null
        spellingSession += 1
        spellingProgressCallback?.invoke(-1)

        tts.stop()
        tts.speak(
            word,
            TextToSpeech.QUEUE_FLUSH,
            speechParameters(),
            "word_${System.nanoTime()}"
        )
    }

    fun spellWord(word: String) {
        if (word.isBlank()) return

        val tts = textToSpeech

        if (!speechReady || tts == null) {
            pendingSpeechAction = { spellWord(word) }
            return
        }

        pendingSpeechAction = null
        tts.stop()

        spellingSession += 1
        val session = spellingSession
        spellingProgressCallback?.invoke(-1)

        if (word.all { it.isDigit() }) {
            tts.speak(
                word,
                TextToSpeech.QUEUE_FLUSH,
                speechParameters(),
                "number_${System.nanoTime()}"
            )
            return
        }

        val letters = word.uppercase(Locale.US)
            .filter { it in 'A'..'Z' }

        if (letters.isEmpty()) {
            speakWord(word)
            return
        }

        letters.forEachIndexed { index, letter ->
            tts.speak(
                letter.toString(),
                if (index == 0) {
                    TextToSpeech.QUEUE_FLUSH
                } else {
                    TextToSpeech.QUEUE_ADD
                },
                speechParameters(),
                "spell:$session:$index"
            )

            if (index < letters.lastIndex) {
                tts.playSilentUtterance(
                    300L,
                    TextToSpeech.QUEUE_ADD,
                    "pause_$index"
                )
            }
        }

        tts.speak(
            word,
            TextToSpeech.QUEUE_ADD,
            speechParameters(),
            "spellDone:$session"
        )
    }

    fun stopSpeech() {
        pendingSpeechAction = null
        spellingSession += 1
        spellingProgressCallback?.invoke(-1)
        textToSpeech?.stop()
    }

    fun shutdown() {
        textToSpeech?.shutdown()
        textToSpeech = null
        speechReady = false
        pendingSpeechAction = null
        spellingProgressCallback = null
        voicesChangedCallback = null
    }
}
