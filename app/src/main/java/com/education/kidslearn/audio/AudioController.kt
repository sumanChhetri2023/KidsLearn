package com.education.kidslearn.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import com.education.kidslearn.R

class AudioController(
    context: Context,
    initialMusicPositionMs: Int = 0,
    initialMusicVolume: Float = 0.25f,
    initialMusicEnabled: Boolean = true
) {

    private val appContext = context.applicationContext
    private val pictureSoundManager = PictureSoundManager(appContext)

    private var musicPlayer: MediaPlayer? = null
    private var soundPool: SoundPool? = null
    private var tapSoundId: Int = 0
    private var soundLoaded = false

    private var musicEnabled = initialMusicEnabled
    private var effectsEnabled = true
    private var released = false
    private var musicVolume = initialMusicVolume.coerceIn(0f, 1f)


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
                    if (!released && sampleId == tapSoundId && status == 0) {
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

            // Restore the previous playback position after activity recreation.
            if (initialMusicPositionMs > 0) {
                val safePosition = initialMusicPositionMs.coerceIn(
                    0,
                    (duration - 1).coerceAtLeast(0)
                )
                seekTo(safePosition)
            }
        }
    }

    fun setMusicEnabled(enabled: Boolean) {
        if (released) return

        musicEnabled = enabled

        if (enabled) {
            startMusic()
        } else {
            pauseMusic()
        }
    }

    fun setMusicVolume(volume: Float) {
        if (released) return

        musicVolume = volume.coerceIn(0f, 1f)

        try {
            musicPlayer?.setVolume(musicVolume, musicVolume)
        } catch (_: IllegalStateException) {
            // Player is no longer available.
        }
    }

    fun setSoundEffectsEnabled(enabled: Boolean) {
        if (released) return
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
                if (!player.isPlaying) {
                    player.start()
                }
            }
        } catch (_: IllegalStateException) {
            // Player is no longer available.
        }
    }

    fun pauseMusic() {
        try {
            musicPlayer?.let { player ->
                if (player.isPlaying) {
                    player.pause()
                }
            }
        } catch (_: IllegalStateException) {
            // Player is no longer available.
        }
    }

    fun getMusicPosition(): Int {
        return try {
            musicPlayer?.currentPosition ?: 0
        } catch (_: IllegalStateException) {
            0
        }
    }

    fun playPictureSound(itemName: String) {
        if (released || !effectsEnabled) return
        pictureSoundManager.play(itemName)
    }

    fun stopPictureSounds() {
        pictureSoundManager.stop()
    }

    fun release() {
        if (released) return

        released = true

        pictureSoundManager.release()

        try {
            musicPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
            }
        } catch (_: IllegalStateException) {
            // Player may already be stopped or unavailable.
        }

        musicPlayer?.release()
        musicPlayer = null

        soundPool?.setOnLoadCompleteListener(null)
        soundPool?.release()
        soundPool = null
        soundLoaded = false
    }
}
