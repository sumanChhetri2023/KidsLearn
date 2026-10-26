package com.education.kidslearn.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import com.education.kidslearn.R

class AudioController(context: Context) {

    private val appContext = context.applicationContext

    private var musicPlayer: MediaPlayer? = null
    private var soundPool: SoundPool? = null
    private var tapSoundId: Int = 0
    private var soundLoaded = false
    private var musicEnabled = true
    private var effectsEnabled = true
    private var released = false
    private var musicVolume = 0.25f

    init {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(attributes)
            .build()
            .also { pool ->
                pool.setOnLoadCompleteListener { _, sampleId, status ->
                    if (sampleId == tapSoundId && status == 0) {
                        soundLoaded = true
                    }
                }
                tapSoundId = pool.load(appContext, R.raw.soft_tap, 1)
            }

        musicPlayer = MediaPlayer.create(
            appContext,
            R.raw.background_music
        )?.apply {
            isLooping = true
            setVolume(musicVolume, musicVolume)
        }
    }

    fun setMusicEnabled(enabled: Boolean) {
        musicEnabled = enabled
        if (enabled) {
            startMusic()
        } else {
            pauseMusic()
        }
    }

    fun setMusicVolume(volume: Float) {
        musicVolume = volume.coerceIn(0f, 1f)
        musicPlayer?.setVolume(musicVolume, musicVolume)
    }

    fun setSoundEffectsEnabled(enabled: Boolean) {
        effectsEnabled = enabled
    }

    fun playTap() {
        if (released || !effectsEnabled || !soundLoaded) return

        soundPool?.play(
            tapSoundId,
            0.45f,
            0.45f,
            1,
            0,
            1f
        )
    }

    fun startMusic() {
        if (released || !musicEnabled) return

        try {
            musicPlayer?.let { player ->
                if (!player.isPlaying) player.start()
            }
        } catch (_: IllegalStateException) {
            // Player is no longer available.
        }
    }

    fun pauseMusic() {
        try {
            musicPlayer?.let { player ->
                if (player.isPlaying) player.pause()
            }
        } catch (_: IllegalStateException) {
            // Player is no longer available.
        }
    }

    fun playPictureSound(itemName: String) {
        if (released) return

        val soundResource = when (itemName.trim().lowercase()) {
            "lion" -> R.raw.lion
            "dog" -> R.raw.dog
            else -> return
        }

        val player = MediaPlayer.create(appContext, soundResource) ?: return

        player.setOnCompletionListener { it.release() }
        player.setOnErrorListener { mp, _, _ ->
            mp.release()
            true
        }

        player.start()
    }

    fun release() {
        if (released) return
        released = true

        try {
            musicPlayer?.stop()
        } catch (_: IllegalStateException) {
            // Player may already be stopped.
        }

        musicPlayer?.release()
        musicPlayer = null

        soundPool?.release()
        soundPool = null
        soundLoaded = false
    }
}
