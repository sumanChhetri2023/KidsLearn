package com.education.kidslearn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.education.kidslearn.audio.AudioController
import com.education.kidslearn.audio.SpeechManager
import com.education.kidslearn.data.LearningItem
import com.education.kidslearn.data.categories
import com.education.kidslearn.data.categoryItems
import com.education.kidslearn.ui.home.HomeScreen
import com.education.kidslearn.ui.learning.ItemPicture as LearningItemPicture
import com.education.kidslearn.ui.learning.LearningScreen
import com.education.kidslearn.ui.quiz.QuizScreen
import com.education.kidslearn.ui.settings.SettingsScreen
import androidx.compose.runtime.remember

val signatureBackground = Color(0xFFFFFBF5)

val canvasColors = listOf(
    Color(0xFFE3F2FD),
    Color(0xFFFFF3E0),
    Color(0xFFE8F5E9),
    Color(0xFFFFEBEE),
    Color(0xFFFFF9C4),
    Color(0xFFEDE7F6),
    Color(0xFFF3E5F5),
    Color(0xFFE0F7FA),
    Color(0xFFFFE0B2),
    Color(0xFFF1F8E9),
    Color(0xFFECEFF1),
    Color(0xFFFFF8E1)
)

private const val SETTINGS_PREFS = "kids_learn_settings"
private const val KEY_MUSIC = "background_music_enabled"
private const val KEY_MUSIC_VOLUME_LEVEL = "background_music_volume_level"
private const val KEY_EFFECTS = "sound_effects_enabled"
private const val KEY_SPOKEN_WORDS = "spoken_words_enabled"
private const val KEY_QUIZ_QUESTION_COUNT = "quiz_question_count"
private const val KEY_QUIZ_CATEGORIES = "quiz_selected_categories"

/**
 * Maps the selected volume option to the actual music volume.
 * 0 = Low, 1 = Mid, 2 = High.
 */
private fun musicVolumeForLevel(level: Int): Float {
    return when (level.coerceIn(0, 2)) {
        0 -> 0.10f
        1 -> 0.25f
        else -> 0.45f
    }
}

fun nextCanvasColor(current: Color): Color {
    return canvasColors.filter { it != current }.random()
}

class MainActivity : ComponentActivity() {

    private lateinit var speechManager: SpeechManager
    private lateinit var audioController: AudioController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        speechManager = SpeechManager(this)
        audioController = AudioController(this)

        val prefs = getSharedPreferences(
            SETTINGS_PREFS,
            MODE_PRIVATE
        )

        setContent {
            var musicEnabled by rememberSaveable {
                mutableStateOf(prefs.getBoolean(KEY_MUSIC, true))
            }

            var musicVolumeLevel by rememberSaveable {
                mutableStateOf(
                    prefs.getInt(KEY_MUSIC_VOLUME_LEVEL, 1).coerceIn(0, 2)
                )
            }

            var effectsEnabled by rememberSaveable {
                mutableStateOf(prefs.getBoolean(KEY_EFFECTS, true))
            }

            var spokenWordsEnabled by rememberSaveable {
                mutableStateOf(prefs.getBoolean(KEY_SPOKEN_WORDS, true))
            }

            val availableQuizCategories = categories
                .filter { it.name != "Quiz" }
                .map { it.name }

            var quizQuestionCount by rememberSaveable {
                mutableStateOf(
                    prefs.getInt(KEY_QUIZ_QUESTION_COUNT, 20)
                        .let { saved ->
                            if (saved in listOf(5, 10, 15, 20)) saved else 20
                        }
                )
            }

            var selectedQuizCategories by remember {
                mutableStateOf(
                    (
                            prefs.getStringSet(KEY_QUIZ_CATEGORIES, null)
                                ?.filter { it in availableQuizCategories }
                                ?.toSet()
                                ?.takeIf { it.isNotEmpty() }
                                ?: availableQuizCategories.toSet()
                            )
                )
            }

            val onQuizQuestionCountChanged: (Int) -> Unit = { count ->
                val safeCount = count.coerceIn(5, 20)
                if (safeCount in listOf(5, 10, 15, 20)) {
                    quizQuestionCount = safeCount
                    prefs.edit()
                        .putInt(KEY_QUIZ_QUESTION_COUNT, safeCount)
                        .apply()
                }
            }

            val onQuizCategoriesChanged: (Set<String>) -> Unit = { selected ->
                val validSelection = selected.intersect(availableQuizCategories.toSet())

                if (validSelection.isNotEmpty()) {
                    selectedQuizCategories = validSelection
                    prefs.edit()
                        .putStringSet(KEY_QUIZ_CATEGORIES, validSelection.toSet())
                        .apply()
                }
            }

            // Apply saved audio preferences to the audio controller.
            audioController.setMusicVolume(musicVolumeForLevel(musicVolumeLevel))
            audioController.setMusicEnabled(musicEnabled)
            audioController.setSoundEffectsEnabled(effectsEnabled)

            val onMusicChanged: (Boolean) -> Unit = { enabled ->
                musicEnabled = enabled

                prefs.edit()
                    .putBoolean(KEY_MUSIC, enabled)
                    .apply()

                audioController.setMusicEnabled(enabled)
                audioController.playTap()
            }

            val onMusicVolumeChanged: (Int) -> Unit = { level ->
                val safeLevel = level.coerceIn(0, 2)

                musicVolumeLevel = safeLevel

                prefs.edit()
                    .putInt(KEY_MUSIC_VOLUME_LEVEL, safeLevel)
                    .apply()

                audioController.setMusicVolume(musicVolumeForLevel(safeLevel)
                )
            }

            val onEffectsChanged: (Boolean) -> Unit = { enabled ->
                effectsEnabled = enabled

                prefs.edit()
                    .putBoolean(KEY_EFFECTS, enabled)
                    .apply()

                audioController.setSoundEffectsEnabled(enabled)

                if (enabled) {audioController.playTap()}
            }

            val onSpokenWordsChanged: (Boolean) -> Unit = { enabled ->
                spokenWordsEnabled = enabled

                prefs.edit()
                    .putBoolean(KEY_SPOKEN_WORDS, enabled)
                    .apply()

                if (!enabled) {
                    speechManager.stopSpeech()
                }
            }

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = signatureBackground
                ) {
                    KidsLearnApp(
                        musicEnabled = musicEnabled,
                        musicVolumeLevel = musicVolumeLevel,
                        effectsEnabled = effectsEnabled,
                        spokenWordsEnabled = spokenWordsEnabled,
                        onMusicChanged = onMusicChanged,
                        onMusicVolumeChanged = onMusicVolumeChanged,
                        onEffectsChanged = onEffectsChanged,
                        onSpokenWordsChanged = onSpokenWordsChanged,
                        quizQuestionCount = quizQuestionCount,
                        availableQuizCategories = availableQuizCategories,
                        selectedQuizCategories = selectedQuizCategories,
                        onQuizQuestionCountChanged = onQuizQuestionCountChanged,
                        onQuizCategoriesChanged = onQuizCategoriesChanged,
                        onSpeak = { word ->
                            if (spokenWordsEnabled) {
                                speechManager.speakWord(word)
                            }
                        },
                        onSpell = { word ->
                            if (spokenWordsEnabled) {
                                speechManager.spellWord(word)
                            }
                        },
                        onStopSpeech = {
                            speechManager.stopSpeech()
                        },
                        onSpellingProgress = { callback ->
                            speechManager.setSpellingProgressCallback(callback)
                        },
                        onPlaySoundEffect = {
                            audioController.playTap()
                        },
                        onPlayPictureSound = { itemName ->
                            speechManager.stopSpeech()
                            audioController.playPictureSound(itemName)
                        }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (::audioController.isInitialized) {
            audioController.startMusic()
        }
    }

    override fun onStop() {
        if (::audioController.isInitialized) {
            audioController.pauseMusic()
        }

        if (::speechManager.isInitialized) {
            speechManager.stopSpeech()
        }

        super.onStop()
    }

    override fun onDestroy() {
        if (::speechManager.isInitialized) {
            speechManager.stopSpeech()
            speechManager.shutdown()
        }

        if (::audioController.isInitialized) {
            audioController.release()
        }

        super.onDestroy()
    }
}

@Composable
private fun KidsLearnApp(
    musicEnabled: Boolean,
    musicVolumeLevel: Int,
    effectsEnabled: Boolean,
    spokenWordsEnabled: Boolean,
    onMusicChanged: (Boolean) -> Unit,
    onMusicVolumeChanged: (Int) -> Unit,
    onEffectsChanged: (Boolean) -> Unit,
    onSpokenWordsChanged: (Boolean) -> Unit,
    onSpeak: (String) -> Unit,
    onSpell: (String) -> Unit,
    onStopSpeech: () -> Unit,
    onSpellingProgress: (((Int) -> Unit)) -> Unit,
    onPlaySoundEffect: () -> Unit,
    onPlayPictureSound: (String) -> Unit,
    quizQuestionCount: Int,
    availableQuizCategories: List<String>,
    selectedQuizCategories: Set<String>,
    onQuizQuestionCountChanged: (Int) -> Unit,
    onQuizCategoriesChanged: (Set<String>) -> Unit

) {
    var selectedCategoryName by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var showSettings by rememberSaveable {
        mutableStateOf(false)
    }

    val selectedCategory = categories.firstOrNull {
        it.name == selectedCategoryName
    }

    BackHandler(enabled = selectedCategory != null || showSettings) {
        onStopSpeech()

        if (showSettings) {
            showSettings = false
        } else {
            selectedCategoryName = null
        }
    }

    when {
        showSettings -> {
            SettingsScreen(
                onBackClick = {
                    onStopSpeech()
                    showSettings = false
                },
                musicEnabled = musicEnabled,
                onMusicChanged = onMusicChanged,
                musicVolumeLevel = musicVolumeLevel,
                onMusicVolumeChanged = onMusicVolumeChanged,
                effectsEnabled = effectsEnabled,
                onEffectsChanged = onEffectsChanged,
                spokenWordsEnabled = spokenWordsEnabled,
                onSpokenWordsChanged = onSpokenWordsChanged,
                quizQuestionCount = quizQuestionCount,
                onQuizQuestionCountChanged = onQuizQuestionCountChanged,
                availableQuizCategories = availableQuizCategories,
                selectedQuizCategories = selectedQuizCategories,
                onQuizCategoriesChanged = onQuizCategoriesChanged
            )
        }

        selectedCategory == null -> {
            HomeScreen(
                onCategorySelected = { category ->
                    onStopSpeech()
                    onPlaySoundEffect()
                    selectedCategoryName = category.name
                },
                onSettingsClick = {
                    onStopSpeech()
                    showSettings = true
                }
            )
        }

        else -> {
            LearningScreen(
                category = selectedCategory,
                items = if (selectedCategory.name == "Quiz") {
                    categories
                        .filter {
                            it.name != "Quiz" &&
                                    it.name in selectedQuizCategories
                        }
                        .flatMap { categoryItems[it.name].orEmpty() }
                        .distinctBy { it.name }
                } else {
                    categoryItems[selectedCategory.name].orEmpty()
                },
                quizQuestionCount = quizQuestionCount,
                onBack = {
                    onStopSpeech()
                    selectedCategoryName = null
                },
                onSpeak = onSpeak,
                onSpell = onSpell,
                onSpellingProgress = onSpellingProgress,
                onPlayPictureSound = onPlayPictureSound
            )
        }
    }
}

@Composable
fun QuizContent(
    modifier: Modifier = Modifier,
    onHome: () -> Unit,
    item: LearningItem,
    options: List<LearningItem>,
    bitmapCache: MutableMap<String, ImageBitmap>,
    isLandscape: Boolean,
    feedback: String?,
    answeredCorrectly: Boolean,
    questionNumber: Int,
    totalQuestions: Int,
    score: Int,
    quizComplete: Boolean,
    onReplay: () -> Unit,
    onChoose: (LearningItem) -> Unit,
    onNext: () -> Unit,
    onRestart: () -> Unit
) {
    QuizScreen(
        modifier = modifier,
        onHome = onHome,
        item = item,
        options = options,
        bitmapCache = bitmapCache,
        isLandscape = isLandscape,
        feedback = feedback,
        answeredCorrectly = answeredCorrectly,
        questionNumber = questionNumber,
        totalQuestions = totalQuestions,
        score = score,
        quizComplete = quizComplete,
        onReplay = onReplay,
        onChoose = onChoose,
        onNext = onNext,
        onRestart = onRestart
    )
}

@Composable
fun ItemPicture(
    item: LearningItem,
    bitmapCache: MutableMap<String, ImageBitmap>,
    modifier: Modifier = Modifier,
    animationScale: Float = 1f
) {
    LearningItemPicture(
        item = item,
        bitmapCache = bitmapCache,
        modifier = modifier,
        animationScale = animationScale
    )
}
