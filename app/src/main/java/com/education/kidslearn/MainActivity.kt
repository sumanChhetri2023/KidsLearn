
package com.education.kidslearn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class LearningCategory(
    val name: String,
    val emoji: String,
    val color: Color
)

private val categories = listOf(
    LearningCategory("Animals", "🐘", Color(0xFFFFE0B2)),
    LearningCategory("Birds", "🦜", Color(0xFFC8E6C9)),
    LearningCategory("Vehicles", "🚗", Color(0xFFBBDEFB)),
    LearningCategory("Fruits", "🍎", Color(0xFFFFCDD2)),
    LearningCategory("Flowers", "🌻", Color(0xFFFFF9C4)),
    LearningCategory("Clothes", "👕", Color(0xFFD1C4E9)),
    LearningCategory("Insects", "🦋", Color(0xFFB2DFDB)),
    LearningCategory("Food", "🍕", Color(0xFFFFCCBC)),
    LearningCategory("Numbers", "🔢", Color(0xFFB3E5FC)),
    LearningCategory("Shapes", "⭐", Color(0xFFE1BEE7))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFFFBF2)
                ) {
                    KidsLearnHome()
                }
            }
        }
    }
}

@Composable
private fun KidsLearnHome() {
    val selectedCategory = remember { mutableStateOf<LearningCategory?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF2))
            .padding(16.dp)
    ) {
        Text(
            text = "🌈 Kids Learn",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF3949AB)
        )

        Text(
            text = "Explore, discover and learn!",
            fontSize = 16.sp,
            color = Color(0xFF616161),
            modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
        )

        Text(
            text = selectedCategory.value?.let {
                "${it.emoji} ${it.name}"
            } ?: "Choose a category",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryCard(
                    category = category,
                    onClick = { selectedCategory.value = category }
                )
            }
        }
    }
}

@Composable
private fun CategoryCard(
    category: LearningCategory,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = category.color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = category.emoji, fontSize = 48.sp)
            Text(
                text = category.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263238),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
