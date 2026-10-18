
package com.education.kidslearn

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private data class LearningCategory(
    val name: String,
    val emoji: String,
    val color: Color
)

private data class LearningItem(
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

private val animals = listOf(
    LearningItem("Lion", "🦁", Color(0xFFFFE0B2)),
    LearningItem("Elephant", "🐘", Color(0xFFBBDEFB)),
    LearningItem("Tiger", "🐅", Color(0xFFFFCCBC)),
    LearningItem("Monkey", "🐒", Color(0xFFD7CCC8)),
    LearningItem("Giraffe", "🦒", Color(0xFFFFF9C4)),
    LearningItem("Rabbit", "🐇", Color(0xFFF8BBD0)),
    LearningItem("Bear", "🐻", Color(0xFFD7CCC8)),
    LearningItem("Dog", "🐕", Color(0xFFC8E6C9)),
    LearningItem("Cat", "🐈", Color(0xFFE1BEE7)),
    LearningItem("Cow", "🐄", Color(0xFFBBDEFB)),
    LearningItem("Horse", "🐎", Color(0xFFFFE0B2)),
    LearningItem("Fox", "🦊", Color(0xFFFFCCBC)),
    LearningItem("Panda", "🐼", Color(0xFFE0E0E0)),
    LearningItem("Sheep", "🐑", Color(0xFFF5F5F5)),
    LearningItem("Pig", "🐖", Color(0xFFF8BBD0)),
    LearningItem("Goat", "🐐", Color(0xFFD7CCC8)),
    LearningItem("Deer", "🦌", Color(0xFFFFF9C4)),
    LearningItem("Wolf", "🐺", Color(0xFFCFD8DC)),
    LearningItem("Frog", "🐸", Color(0xFFC8E6C9)),
    LearningItem("Turtle", "🐢", Color(0xFFB2DFDB))
)

private val birds = listOf(
    LearningItem("Parrot", "🦜", Color(0xFFC8E6C9)),
    LearningItem("Peacock", "🦚", Color(0xFFBBDEFB)),
    LearningItem("Sparrow", "🐦", Color(0xFFFFE0B2)),
    LearningItem("Eagle", "🦅", Color(0xFFD7CCC8)),
    LearningItem("Owl", "🦉", Color(0xFFE1BEE7)),
    LearningItem("Pigeon", "🕊️", Color(0xFFCFD8DC)),
    LearningItem("Duck", "🦆", Color(0xFFFFF9C4)),
    LearningItem("Chicken", "🐔", Color(0xFFFFCCBC)),
    LearningItem("Rooster", "🐓", Color(0xFFF8BBD0)),
    LearningItem("Swan", "🦢", Color(0xFFE0F2F1)),
    LearningItem("Flamingo", "🦩", Color(0xFFF8BBD0)),
    LearningItem("Turkey", "🦃", Color(0xFFD7CCC8)),
    LearningItem("Penguin", "🐧", Color(0xFFBBDEFB)),
    LearningItem("Woodpecker", "🐦", Color(0xFFFFCDD2)),
    LearningItem("Kingfisher", "🐦", Color(0xFFB2DFDB)),
    LearningItem("Crow", "🐦‍⬛", Color(0xFFCFD8DC)),
    LearningItem("Hummingbird", "🐦", Color(0xFFD1C4E9)),
    LearningItem("Seagull", "🕊️", Color(0xFFE0E0E0)),
    LearningItem("Ostrich", "🪶", Color(0xFFFFE0B2)),
    LearningItem("Cuckoo", "🐦", Color(0xFFFFF9C4))
)

class MainActivity : ComponentActivity() {

    private var textToSpeech: TextToSpeech? = null
    private var ttsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeech = TextToSpeech(this) { status ->
            ttsReady = status == TextToSpeech.SUCCESS

            if (ttsReady) {
                val result = textToSpeech?.setLanguage(Locale.UK)
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    ttsReady = false
                }
            }
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFFFBF2)
                ) {
                    KidsLearnApp(
                        onSpeak = { word ->
                            if (ttsReady) {
                                textToSpeech?.speak(
                                    word,
                                    TextToSpeech.QUEUE_FLUSH,
                                    null,
                                    "kidslearn-$word"
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        super.onDestroy()
    }
}

@Composable
private fun KidsLearnApp(onSpeak: (String) -> Unit) {
    var selectedCategory by remember {
        mutableStateOf<LearningCategory?>(null)
    }

    BackHandler(enabled = selectedCategory != null) {
        selectedCategory = null
    }

    when (selectedCategory?.name) {
        "Animals" -> LearningScreen(
            title = "Meet the Animals 🐾",
            instruction = "Tap any animal to learn its name!",
            items = animals,
            onBack = { selectedCategory = null },
            onSpeak = onSpeak
        )

        "Birds" -> LearningScreen(
            title = "Meet the Birds 🐦",
            instruction = "Tap any bird to learn its name!",
            items = birds,
            onBack = { selectedCategory = null },
            onSpeak = onSpeak
        )

        else -> HomeScreen(
            selectedCategory = selectedCategory,
            onCategoryClick = { selectedCategory = it }
        )
    }
}

@Composable
private fun HomeScreen(
    selectedCategory: LearningCategory?,
    onCategoryClick: (LearningCategory) -> Unit
) {
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
            text = selectedCategory?.let { "${it.emoji} ${it.name}" }
                ?: "Choose a category",
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
                CategoryCard(category) {
                    onCategoryClick(category)
                }
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

@Composable
private fun LearningScreen(
    title: String,
    instruction: String,
    items: List<LearningItem>,
    onBack: () -> Unit,
    onSpeak: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF2))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text("← Back", fontSize = 16.sp)
            }

            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF3949AB),
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Text(
            text = instruction,
            fontSize = 15.sp,
            color = Color(0xFF616161),
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item ->
                LearningItemCard(item = item) {
                    onSpeak(item.name)
                }
            }
        }
    }
}

@Composable
private fun LearningItemCard(
    item: LearningItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = item.color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(76.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.emoji, fontSize = 52.sp)
            }

            Text(
                text = item.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263238),
                modifier = Modifier.padding(top = 6.dp)
            )

            Text(
                text = "🔊 Tap to hear",
                fontSize = 12.sp,
                color = Color(0xFF546E7A),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
