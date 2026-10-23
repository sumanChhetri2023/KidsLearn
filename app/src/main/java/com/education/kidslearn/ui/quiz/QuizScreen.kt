package com.education.kidslearn.ui.quiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.education.kidslearn.data.LearningItem
import com.education.kidslearn.ui.learning.ItemPicture

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
    if (quizComplete) {
        Column(
            modifier = modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val celebration = rememberInfiniteTransition(label = "quizCelebration")
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
                text = if (score >= 16) "🎉 ✨ 🏆 ✨ 🎉" else if (score >= 10) "🎊 🌟 🎊" else "🌈 ⭐ 🌈",
                fontSize = 34.sp,
                modifier = Modifier.scale(celebrationScale).padding(bottom = 12.dp)
            )
            Text(
                text = when {
                    score >= 18 -> "Amazing work!"
                    score >= 14 -> "Excellent effort!"
                    score >= 10 -> "Great practice!"
                    else -> "Keep learning!"
                },
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
                text = "Your score: $score / $totalQuestions",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32),
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "${(score * 100) / totalQuestions.coerceAtLeast(1)}% correct on the first try",
                fontSize = 20.sp,
                color = Color(0xFF37474F),
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )
            Button(onClick = onRestart) {
                Text("Play again", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onHome,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF546E7A))
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
            text = "Find the ${item.name}!",
            fontSize = if (isLandscape) 25.sp else 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF263238),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Button(
            onClick = onReplay,
            modifier = Modifier.padding(bottom = 12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5))
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
            // Portrait: four answer cards arranged as two columns and two rows.
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                options.chunked(2).forEach { rowOptions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                        if (rowOptions.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        if (feedback != null) {
            Text(
                text = feedback,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = if (answeredCorrectly) Color(0xFF2E7D32) else Color(0xFFEF6C00),
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
            )
        }

        if (answeredCorrectly) {
            Button(
                onClick = onNext,
                modifier = Modifier.padding(top = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text(if (questionNumber >= totalQuestions) "See results" else "Next question", fontWeight = FontWeight.Bold)
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
            .height(128.dp)
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = option.color),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ItemPicture(
                item = option,
                bitmapCache = bitmapCache,
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
            Text(
                text = option.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263238)
            )
        }
    }
}
