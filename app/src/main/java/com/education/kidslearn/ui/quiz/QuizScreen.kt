package com.education.kidslearn.ui.quiz

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.education.kidslearn.data.LearningItem
import com.education.kidslearn.ui.learning.ItemPicture
import kotlinx.coroutines.delay
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.draw.clip

private const val AUTO_ADVANCE_DELAY_MS = 1500L

@Composable
fun QuizScreen(
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
    LaunchedEffect(questionNumber, answeredCorrectly, quizComplete) {
        if (answeredCorrectly && !quizComplete) {
            delay(AUTO_ADVANCE_DELAY_MS)
            onNext()
        }
    }

    if (quizComplete) {
        val safeTotalQuestions = totalQuestions.coerceAtLeast(1)
        val safeScore = score.coerceIn(0, safeTotalQuestions)
        val accuracyPercent = safeScore * 100 / safeTotalQuestions

        val resultMessage = when {
            accuracyPercent >= 90 -> "Amazing work!"
            accuracyPercent >= 75 -> "Excellent effort!"
            accuracyPercent >= 50 -> "Great practice!"
            else -> "Keep learning!"
        }

        val celebrationEmoji = when {
            accuracyPercent >= 90 -> "🎉 ✨ 🏆 ✨ 🎉"
            accuracyPercent >= 75 -> "🎊 🌟 🎊"
            accuracyPercent >= 50 -> "🌈 ⭐ 🌈"
            else -> "💪 🌱 💪"
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val celebration = rememberInfiniteTransition(
                label = "quizCelebration"
            )
            val celebrationScale by celebration.animateFloat(
                initialValue = 0.92f,
                targetValue = 1.08f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 650),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "celebrationScale"
            )

            Text(
                text = celebrationEmoji,
                fontSize = 34.sp,
                modifier = Modifier
                    .scale(celebrationScale)
                    .padding(bottom = 12.dp)
            )

            Text(
                text = resultMessage,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Quiz complete!",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263238)
            )

            Text(
                text = "Your score: $safeScore/$safeTotalQuestions ($accuracyPercent% correct)",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32),
                modifier = Modifier.padding(
                    top = 16.dp,
                    bottom = 24.dp
                )
            )

            Button(onClick = onRestart) {
                Text("Play again", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onHome,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF546E7A)
                )
            ) {
                Text("Go to home", fontWeight = FontWeight.Bold)
            }
        }

        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Question $questionNumber of $totalQuestions  •  Score: $score",
            fontSize = if (isLandscape) 16.sp else 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF546E7A),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Text(
            text = if (
                item.name.isNotEmpty() && item.name.all { it.isDigit() }
            ) {
                "Find the number ${item.name}!"
            } else {
                "Find the ${item.name}!"
            },
            fontSize = if (isLandscape) 25.sp else 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF263238),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Button(
            onClick = onReplay,
            modifier = Modifier.padding(bottom = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF42A5F5)
            )
        ) {
            Text("🔊 Replay question", fontWeight = FontWeight.Bold)
        }

        if (isLandscape) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                options.forEach { option ->
                    QuizOption(
                        option = option,
                        bitmapCache = bitmapCache,
                        enabled = !answeredCorrectly,
                        onClick = { onChoose(option) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        } else {
            // Portrait: two columns and two rows.
            options.chunked(2).forEach { rowOptions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    rowOptions.forEach { option ->
                        QuizOption(
                            option = option,
                            bitmapCache = bitmapCache,
                            enabled = !answeredCorrectly,
                            onClick = { onChoose(option) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Keep the final row aligned if it has only one option.
                    if (rowOptions.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        if (feedback != null) {
            Text(
                text = feedback,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = if (answeredCorrectly) {
                    Color(0xFF2E7D32)
                } else {
                    Color(0xFFEF6C00)
                },
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
            )

            // Show this message only when another question remains.
            if (answeredCorrectly && questionNumber < totalQuestions) {
                Text(
                    text = "Next question coming up…",
                    fontSize = 14.sp,
                    color = Color(0xFF546E7A)
                )
            }
        }
    }
}


@Composable
private fun QuizOption(
    option: LearningItem,
    bitmapCache: MutableMap<String, ImageBitmap>,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(150.dp)
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = option.color
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(
                        width = 1.5.dp,
                        color = Color(0xFF546E7A),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                ItemPicture(
                    item = option,
                    bitmapCache = bitmapCache,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

