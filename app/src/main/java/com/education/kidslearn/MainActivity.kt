package com.education.kidslearn

import com.education.kidslearn.audio.SpeechManager
import com.education.kidslearn.ui.quiz.QuizScreen
import com.education.kidslearn.ui.learning.ItemPicture as LearningItemPicture
import com.education.kidslearn.ui.learning.LearningScreen
import com.education.kidslearn.ui.home.HomeScreen
import com.education.kidslearn.data.LearningItem
import com.education.kidslearn.data.categories
import com.education.kidslearn.data.categoryItems
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


val signatureBackground = Color(0xFFFFFBF5)

val canvasColors = listOf(
    Color(0xFFE3F2FD), // soft blue
    Color(0xFFFFF3E0), // warm cream
    Color(0xFFE8F5E9), // soft green
    Color(0xFFFFEBEE), // blush pink
    Color(0xFFFFF9C4), // pale yellow
    Color(0xFFEDE7F6), // lavender
    Color(0xFFF3E5F5), // lilac
    Color(0xFFE0F7FA), // pale aqua
    Color(0xFFFFE0B2), // peach
    Color(0xFFF1F8E9), // light lime
    Color(0xFFECEFF1), // mist grey
    Color(0xFFFFF8E1)  // vanilla
)

fun nextCanvasColor(current: Color): Color {
    val alternatives = canvasColors.filter { it != current }
    return alternatives.random()
}


class MainActivity : ComponentActivity() {

    private lateinit var speechManager: SpeechManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        speechManager = SpeechManager(this)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = signatureBackground
                ) {
                    KidsLearnApp(
                        onSpeak = { speechManager.speakWord(it) },
                        onSpell = { speechManager.spellWord(it) },
                        onStopSpeech = { speechManager.stopSpeech() },
                        onSpellingProgress = { speechManager.setSpellingProgressCallback(it) }
                    )
                }
            }
        }
    }

    override fun onStop() {
        speechManager.stopSpeech()
        super.onStop()
    }

    override fun onDestroy() {
        speechManager.stopSpeech()
        speechManager.shutdown()
        super.onDestroy()
    }
}

@Composable
private fun KidsLearnApp(
    onSpeak: (String) -> Unit,
    onSpell: (String) -> Unit,
    onStopSpeech: () -> Unit,
    onSpellingProgress: (((Int) -> Unit)) -> Unit
) {
    var selectedCategoryName by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    val selectedCategory = categories.firstOrNull { it.name == selectedCategoryName }

    BackHandler(enabled = selectedCategory != null) {
        onStopSpeech()
        selectedCategoryName = null
    }

    if (selectedCategory == null) {
        HomeScreen(
            onCategorySelected = { category ->
                onStopSpeech()
                selectedCategoryName = category.name
            }
        )
    } else {
        val category = selectedCategory

        LearningScreen(
            category = category,
            items = categoryItems[category.name].orEmpty(),
            onBack = {
                onStopSpeech()
                selectedCategoryName = null
            },
            onSpeak = onSpeak,
            onSpell = onSpell,
            onSpellingProgress = onSpellingProgress
        )
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

// Compatibility entry point for existing LearningScreen and QuizContent callers.
// The implementation now lives in ui.learning.ItemPicture.
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
