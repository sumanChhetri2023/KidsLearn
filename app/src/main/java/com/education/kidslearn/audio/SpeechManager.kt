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

    private val appContext = context.applicationContext
    private val mainHandler = Handler(Looper.getMainLooper())
    private val speechLocale = Locale.US

    private var textToSpeech: TextToSpeech? = null

    @Volatile
    private var speechReady = false

    @Volatile
    private var spellingSession = 0L

    private var pendingSpeechAction: (() -> Unit)? = null
    private var spellingProgressCallback: ((Int) -> Unit)? = null
    private var voicesChangedCallback: ((List<SpeechVoice>) -> Unit)? = null

    private var speechVolume = 1.0f
    private var speechRate = 0.85f
    private var selectedVoiceId: String? = null
    private var released = false

    init {
        textToSpeech = TextToSpeech(appContext) { status ->
            if (released) return@TextToSpeech

            val tts = textToSpeech ?: return@TextToSpeech

            if (status != TextToSpeech.SUCCESS) {
                speechReady = false
                pendingSpeechAction = null
                return@TextToSpeech
            }

            val languageResult = tts.setLanguage(speechLocale)

            speechReady =
                languageResult != TextToSpeech.LANG_MISSING_DATA &&
                        languageResult != TextToSpeech.LANG_NOT_SUPPORTED

            if (!speechReady) {
                pendingSpeechAction = null
                return@TextToSpeech
            }

            applySpeechSettings()
            installUtteranceListener(tts)

            voicesChangedCallback?.invoke(getAvailableVoices())

            val action = pendingSpeechAction
            pendingSpeechAction = null
            action?.invoke()
        }
    }

    private fun installUtteranceListener(tts: TextToSpeech) {
        tts.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {

                override fun onStart(utteranceId: String?) {
                    val parts = utteranceId?.split(":") ?: return

                    if (parts.size != 3 || parts[0] != "spell") {
                        return
                    }

                    val session = parts[1].toLongOrNull() ?: return
                    val index = parts[2].toIntOrNull() ?: return

                    dispatchSpellingProgress(session, index)
                }

                override fun onDone(utteranceId: String?) {
                    val parts = utteranceId?.split(":") ?: return

                    if (parts.size != 2 || parts[0] != "spellDone") {
                        return
                    }

                    val session = parts[1].toLongOrNull() ?: return

                    dispatchSpellingProgress(session, -1)
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    handleUtteranceError(utteranceId)
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    handleUtteranceError(utteranceId)
                }
            }
        )
    }

    /**
     * Posts progress to the main thread and validates the session again
     * when the callback executes. This prevents old speech callbacks
     * from changing the progress of a newer spelling session.
     */
    private fun dispatchSpellingProgress(
        session: Long,
        index: Int
    ) {
        mainHandler.post {
            if (
                !released &&
                session == spellingSession
            ) {
                spellingProgressCallback?.invoke(index)
            }
        }
    }

    private fun handleUtteranceError(utteranceId: String?) {
        val parts = utteranceId?.split(":") ?: return

        if (parts.size != 3 || parts[0] != "spell") {
            if (parts.size != 2 || parts[0] != "spellDone") {
                return
            }
        }

        val session = parts[1].toLongOrNull() ?: return
        dispatchSpellingProgress(session, -1)
    }

    private fun applySpeechSettings() {
        val tts = textToSpeech ?: return

        tts.setSpeechRate(
            speechRate.coerceIn(0.5f, 2.0f)
        )

        val voice = selectedVoiceId?.let { id ->
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

    fun setSpellingProgressCallback(callback: ((Int) -> Unit)?)
    {spellingProgressCallback = callback}

    /**
     * Notifies the caller when TTS has initialized
     * and available English voices can be queried.
     */
    fun setVoicesChangedCallback(
        callback: ((List<SpeechVoice>) -> Unit)?
    ) {
        voicesChangedCallback = callback

        if (speechReady && !released) {
            callback?.invoke(getAvailableVoices())
        }
    }

    /**
     * Returns available English voices.
     * US English voices are listed first when available.
     */
    fun getAvailableVoices(): List<SpeechVoice> {
        val tts = textToSpeech ?: return emptyList()

        if (!speechReady || released) {
            return emptyList()
        }

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
     * Sets speech volume from 0.0 (silent) to 1.0 (full volume).
     */
    fun setSpeechVolume(volume: Float) {
        speechVolume = volume.coerceIn(0.0f, 1.0f)
    }

    /**
     * Sets speech rate. 1.0 is the engine's normal rate.
     */
    fun setSpeechSpeed(rate: Float) {
        speechRate = rate.coerceIn(0.5f, 2.0f)

        if (speechReady && !released) {
            textToSpeech?.setSpeechRate(speechRate)
        }
    }

    /**
     * Selects an installed English voice by its engine-provided name.
     * Pass null to use the default US English voice.
     */
    fun setVoice(voiceId: String?) {
        if (released) return

        if (voiceId == null) {
            selectedVoiceId = null

            if (speechReady) {
                textToSpeech?.setLanguage(speechLocale)
            }

            return
        }

        val tts = textToSpeech ?: return
        val voice = tts.voices?.firstOrNull {
            it.name == voiceId && it.locale.language == "en"
        }

        if (voice != null) {
            selectedVoiceId = voice.name

            if (speechReady) {
                tts.voice = voice
            }
        }
    }

    fun speakWord(word: String) {
        if (word.isBlank() || released) return

        val tts = textToSpeech

        if (!speechReady || tts == null) {
            pendingSpeechAction = { speakWord(word) }
            return
        }

        pendingSpeechAction = null
        spellingSession++

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
        if (word.isBlank() || released) return

        val tts = textToSpeech

        if (!speechReady || tts == null) {
            pendingSpeechAction = { spellWord(word) }
            return
        }

        pendingSpeechAction = null

        tts.stop()

        spellingSession++
        val session = spellingSession

        spellingProgressCallback?.invoke(-1)

        // Numbers are spoken as numbers rather than spelled digit by digit.
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
                    "pause:$session:$index"
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
        if (released) return

        pendingSpeechAction = null
        spellingSession++

        spellingProgressCallback?.invoke(-1)

        textToSpeech?.stop()
    }

    fun shutdown() {
        if (released) return

        released = true
        speechReady = false
        spellingSession++

        pendingSpeechAction = null
        spellingProgressCallback = null
        voicesChangedCallback = null

        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}
