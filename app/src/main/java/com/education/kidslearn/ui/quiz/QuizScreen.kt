package com.education.kidslearn.ui.quiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    item: LearningItem,
    options: List<LearningItem>,
    bitmapCache: MutableMap<String, ImageBitmap>,
    isLandscape: Boolean,
    feedback: String?,
    answeredCorrectly: Boolean,
    onReplay: () -> Unit,
    onChoose: (LearningItem) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
                        enabled = feedback == null || !answeredCorrectly,
                        onClick = { onChoose(option) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                options.forEach { option ->
                    QuizOption(
                        option = option,
                        bitmapCache = bitmapCache,
                        enabled = feedback == null || !answeredCorrectly,
                        onClick = { onChoose(option) },
                        modifier = Modifier.fillMaxWidth()
                    )
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
                Text("Next question", fontWeight = FontWeight.Bold)
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
