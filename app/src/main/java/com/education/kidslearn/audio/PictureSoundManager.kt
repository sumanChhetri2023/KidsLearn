package com.education.kidslearn.audio

import android.content.Context
import android.media.MediaPlayer
import java.io.File

/**
 * Plays category-specific picture sounds stored in app/src/main/assets/sounds/.
 * Animal sound files belong in assets/sounds/animals/ and should be named
 * after the learning item, for example lion.mp3 or elephant.mp3.
 */
class PictureSoundManager(context: Context) {

    private val appContext = context.applicationContext
    private var mediaPlayer: MediaPlayer? = null
    private var released = false

    private val animalSoundNames: Set<String> by lazy {
        appContext.assets.list("sounds/animals")
            .orEmpty()
            .filter { it.endsWith(".mp3", ignoreCase = true) }
            .map { it.substringBeforeLast('.').lowercase() }
            .toSet()
    }

    fun hasSound(itemName: String): Boolean {
        return !released && itemName.trim().lowercase() in animalSoundNames
    }

    fun play(itemName: String) {
        stop()
        if (released) return

        val soundName = itemName.trim().lowercase()
        if (soundName !in animalSoundNames) return

        val assetPath = "sounds/animals/$soundName.mp3"
        val cachedFile = File(appContext.cacheDir, "picture_sounds/$soundName.mp3")

        try {
            cachedFile.parentFile?.mkdirs()
            appContext.assets.open(assetPath).use { input ->
                cachedFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val player = MediaPlayer()
            mediaPlayer = player

            player.setOnCompletionListener { completedPlayer ->
                releasePlayer(completedPlayer)
            }
            player.setOnErrorListener { failedPlayer, _, _ ->
                releasePlayer(failedPlayer)
                true
            }
            player.setOnPreparedListener { preparedPlayer ->
                if (!released && mediaPlayer === preparedPlayer) {
                    runCatching { preparedPlayer.start() }
                        .onFailure { releasePlayer(preparedPlayer) }
                } else {
                    releasePlayer(preparedPlayer)
                }
            }

            player.setDataSource(cachedFile.absolutePath)
            player.prepareAsync()
        } catch (_: Exception) {
            stop()
        }
    }

    private fun releasePlayer(player: MediaPlayer) {
        if (mediaPlayer === player) {
            mediaPlayer = null
        }
        runCatching { player.setOnCompletionListener(null) }
        runCatching { player.setOnErrorListener(null) }
        runCatching { player.setOnPreparedListener(null) }
        runCatching { player.release() }
    }

    fun stop() {
        val player = mediaPlayer ?: return
        mediaPlayer = null

        runCatching {
            player.setOnCompletionListener(null)
            player.setOnErrorListener(null)
            player.setOnPreparedListener(null)
            if (player.isPlaying) player.stop()
            player.release()
        }
    }

    fun release() {
        if (released) return
        stop()
        released = true
    }
}
