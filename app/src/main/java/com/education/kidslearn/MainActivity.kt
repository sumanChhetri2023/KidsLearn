package com.education.kidslearn

import android.graphics.BitmapFactory
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlin.random.Random

private val pageBackground = Color(0xFFFFFBF2)
private val darkText = Color(0xFF263238)
private val cardBorder = Color(0xFFE1DDD4)

private val pastelColors = listOf(
    Color(0xFFE0F2E9), // Mint
    Color(0xFFFFF1C9), // Pale yellow
    Color(0xFFDCEEFF), // Sky blue
    Color(0xFFEDE1FF), // Lavender
    Color(0xFFFFE4D6), // Peach
    Color(0xFFFCE4EC), // Pink
    Color(0xFFE0F7FA), // Aqua
    Color(0xFFF1F8E9), // Pale green
    Color(0xFFFFF3E0), // Cream
    Color(0xFFE8EAF6)  // Pale indigo
)

private data class LearningCategory(
    val name: String,
    val emoji: String,
    val color: Color
)

private data class LearningItem(
    val name: String,
    val emoji: String,
    val color: Color,
    val assetPath: String? = null
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
    LearningItem("Lion", "🦁", Color(0xFFFFE0B2), "animals/lion.png"),
    LearningItem("Elephant", "🐘", Color(0xFFE0E0E0), "animals/elephant.png"),
    LearningItem("Tiger", "🐯", Color(0xFFFFCC80), "animals/tiger.png"),
    LearningItem("Rabbit", "🐰", Color(0xFFF8BBD0), "animals/rabbit.png"),
    LearningItem("Panda", "🐼", Color(0xFFE0E0E0), "animals/panda.png"),
    LearningItem("Monkey", "🐒", Color(0xFFD7CCC8), "animals/monkey.png"),
    LearningItem("Dog", "🐶", Color(0xFFFFE0B2), "animals/dog.png"),
    LearningItem("Cat", "🐱", Color(0xFFFFCCBC), "animals/cat.png"),
    LearningItem("Cow", "🐮", Color(0xFFF5F5F5), "animals/cow.png"),
    LearningItem("Horse", "🐴", Color(0xFFD7CCC8), "animals/horse.png"),
    LearningItem("Goat", "🐐", Color(0xFFE6EE9C), "animals/goat.png"),
    LearningItem("Sheep", "🐑", Color(0xFFF5F5F5), "animals/sheep.png"),
    LearningItem("Giraffe", "🦒", Color(0xFFFFF59D), "animals/giraffe.png"),
    LearningItem("Zebra", "🦓", Color(0xFFE0E0E0), "animals/zebra.png"),
    LearningItem("Bear", "🐻", Color(0xFFD7CCC8), "animals/bear.png"),
    LearningItem("Fox", "🦊", Color(0xFFFFCC80), "animals/fox.png"),
    LearningItem("Deer", "🦌", Color(0xFFFFE0B2), "animals/deer.png"),
    LearningItem("Wolf", "🐺", Color(0xFFCFD8DC), "animals/wolf.png"),
    LearningItem("Kangaroo", "🦘", Color(0xFFD7CCC8), "animals/kangaroo.png"),
    LearningItem("Hippo", "🦛", Color(0xFFD1C4E9), "animals/hippo.png")
)

private val birds = listOf(
    LearningItem("Parrot", "🦜", Color(0xFFC8E6C9)),
    LearningItem("Sparrow", "🐦", Color(0xFFFFE0B2)),
    LearningItem("Peacock", "🦚", Color(0xFFB2DFDB)),
    LearningItem("Owl", "🦉", Color(0xFFD7CCC8)),
    LearningItem("Eagle", "🦅", Color(0xFFCFD8DC)),
    LearningItem("Duck", "🦆", Color(0xFFFFF9C4)),
    LearningItem("Chicken", "🐔", Color(0xFFFFCCBC)),
    LearningItem("Rooster", "🐓", Color(0xFFFFCDD2)),
    LearningItem("Swan", "🦢", Color(0xFFF5F5F5)),
    LearningItem("Flamingo", "🦩", Color(0xFFF8BBD0)),
    LearningItem("Penguin", "🐧", Color(0xFFE0E0E0)),
    LearningItem("Ostrich", "🐦", Color(0xFFD7CCC8)),
    LearningItem("Pigeon", "🕊️", Color(0xFFCFD8DC)),
    LearningItem("Kingfisher", "🐦", Color(0xFFBBDEFB)),
    LearningItem("Woodpecker", "🐦", Color(0xFFFFCDD2)),
    LearningItem("Crow", "🐦‍⬛", Color(0xFFCFD8DC)),
    LearningItem("Turkey", "🦃", Color(0xFFFFCC80)),
    LearningItem("Seagull", "🕊️", Color(0xFFE0F7FA)),
    LearningItem("Cuckoo", "🐦", Color(0xFFD7CCC8)),
    LearningItem("Hummingbird", "🐦", Color(0xFFB2DFDB))
)

private fun sampleItems(category: String): List<LearningItem> {
    return when (category) {
        "Vehicles" -> listOf(
            LearningItem("Car", "🚗", Color(0xFFBBDEFB)),
            LearningItem("Bus", "🚌", Color(0xFFFFF9C4)),
            LearningItem("Train", "🚂", Color(0xFFFFCCBC)),
            LearningItem("Airplane", "✈️", Color(0xFFE1BEE7)),
            LearningItem("Bicycle", "🚲", Color(0xFFC8E6C9)),
            LearningItem("Boat", "⛵", Color(0xFFB3E5FC))
        )

        "Fruits" -> listOf(
            LearningItem("Apple", "🍎", Color(0xFFFFCDD2)),
            LearningItem("Banana", "🍌", Color(0xFFFFF9C4)),
            LearningItem("Orange", "🍊", Color(0xFFFFE0B2)),
            LearningItem("Grapes", "🍇", Color(0xFFD1C4E9)),
            LearningItem("Mango", "🥭", Color(0xFFFFCC80)),
            LearningItem("Watermelon", "🍉", Color(0xFFC8E6C9))
        )

        "Flowers" -> listOf(
            LearningItem("Rose", "🌹", Color(0xFFFFCDD2)),
            LearningItem("Sunflower", "🌻", Color(0xFFFFF9C4)),
            LearningItem("Tulip", "🌷", Color(0xFFF8BBD0)),
            LearningItem("Lotus", "🪷", Color(0xFFE1BEE7)),
            LearningItem("Hibiscus", "🌺", Color(0xFFFFCCBC)),
            LearningItem("Daisy", "🌼", Color(0xFFFFF9C4))
        )

        "Clothes" -> listOf(
            LearningItem("Shirt", "👕", Color(0xFFBBDEFB)),
            LearningItem("Dress", "👗", Color(0xFFF8BBD0)),
            LearningItem("Trousers", "👖", Color(0xFFD1C4E9)),
            LearningItem("Shoes", "👟", Color(0xFFE0E0E0)),
            LearningItem("Hat", "🎩", Color(0xFFFFE0B2)),
            LearningItem("Socks", "🧦", Color(0xFFFFCCBC))
        )

        "Insects" -> listOf(
            LearningItem("Butterfly", "🦋", Color(0xFFD1C4E9)),
            LearningItem("Bee", "🐝", Color(0xFFFFF9C4)),
            LearningItem("Ant", "🐜", Color(0xFFFFCCBC)),
            LearningItem("Ladybug", "🐞", Color(0xFFFFCDD2)),
            LearningItem("Caterpillar", "🐛", Color(0xFFC8E6C9)),
            LearningItem("Dragonfly", "🪰", Color(0xFFB2DFDB))
        )

        "Food" -> listOf(
            LearningItem("Pizza", "🍕", Color(0xFFFFCCBC)),
            LearningItem("Bread", "🍞", Color(0xFFFFE0B2)),
            LearningItem("Rice", "🍚", Color(0xFFF5F5F5)),
            LearningItem("Egg", "🥚", Color(0xFFFFF9C4)),
            LearningItem("Cheese", "🧀", Color(0xFFFFF59D)),
            LearningItem("Cake", "🎂", Color(0xFFF8BBD0))
        )

        "Numbers" -> (1..20).map {
            LearningItem(it.toString(), "🔢", Color(0xFFB3E5FC))
        }

        "Shapes" -> listOf(
            LearningItem("Circle", "⚪", Color(0xFFE0F7FA)),
            LearningItem("Square", "🟥", Color(0xFFFFCDD2)),
            LearningItem("Triangle", "🔺", Color(0xFFFFF9C4)),
            LearningItem("Star", "⭐", Color(0xFFFFF9C4)),
            LearningItem("Heart", "❤️", Color(0xFFF8BBD0)),
            LearningItem("Diamond", "🔷", Color(0xFFBBDEFB))
        )

        else -> emptyList()
    }
}

class MainActivity : ComponentActivity() {

    private var textToSpeech: TextToSpeech? = null
    private var speechReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale.UK)
                speechReady =
                    result != TextToSpeech.LANG_MISSING_DATA &&
                            result != TextToSpeech.LANG_NOT_SUPPORTED
            }
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = pageBackground
                ) {
                    KidsLearnApp { word ->
                        if (speechReady) {
                            textToSpeech?.speak(
                                word,
                                TextToSpeech.QUEUE_FLUSH,
                                null,
                                word
                            )
                        }
                    }
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
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    BackHandler(enabled = selectedCategory != null) {
        selectedCategory = null
    }

    if (selectedCategory == null) {
        HomeScreen(
            onCategorySelected = { selectedCategory = it }
        )
    } else {
        val items = when (selectedCategory) {
            "Animals" -> animals
            "Birds" -> birds
            else -> sampleItems(selectedCategory!!)
        }

        SingleItemLearningScreen(
            title = selectedCategory!!,
            items = items,
            onBack = { selectedCategory = null },
            onSpeak = onSpeak
        )
    }
}

@Composable
private fun HomeScreen(
    onCategorySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBackground)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Text(
            text = "Kids Learn",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF37474F)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Explore, learn and have fun!",
            fontSize = 16.sp,
            color = Color(0xFF616161)
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.05f)
                        .clickable {
                            onCategorySelected(category.name)
                        },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = category.color
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = category.emoji,
                            fontSize = 48.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = category.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = darkText,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SingleItemLearningScreen(
    title: String,
    items: List<LearningItem>,
    onBack: () -> Unit,
    onSpeak: (String) -> Unit
) {
    var currentIndex by remember(title) {
        mutableIntStateOf(0)
    }

    // New background and mirror choice are generated for each item visit.
    var visitId by remember(title) {
        mutableIntStateOf(0)
    }

    var backgroundColor by remember(title) {
        mutableStateOf(pastelColors.random())
    }

    var flipHorizontally by remember(title) {
        mutableStateOf(Random.nextBoolean())
    }

    if (items.isEmpty()) return

    val item = items[currentIndex]

    fun moveTo(newIndex: Int) {
        if (newIndex in items.indices && newIndex != currentIndex) {
            currentIndex = newIndex
            visitId++

            backgroundColor = pastelColors
                .filter { it != backgroundColor }
                .random()

            flipHorizontally = Random.nextBoolean()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "‹",
                fontSize = 38.sp,
                color = Color(0xFF37474F),
                modifier = Modifier
                    .clickable(onClick = onBack)
                    .padding(horizontal = 10.dp)
            )

            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF37474F)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${currentIndex + 1} / ${items.size}",
                fontSize = 14.sp,
                color = Color(0xFF757575)
            )
        }

        Text(
            text = "Swipe left or right to explore!",
            fontSize = 14.sp,
            color = Color(0xFF757575),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        LargeLearningImage(
            item = item,
            backgroundColor = backgroundColor,
            flipHorizontally = flipHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .pointerInput(currentIndex, items.size) {
                    var totalDrag = 0f

                    detectHorizontalDragGestures(
                        onDragEnd = {
                            when {
                                totalDrag < -80f &&
                                        currentIndex < items.lastIndex -> {
                                    moveTo(currentIndex + 1)
                                }

                                totalDrag > 80f &&
                                        currentIndex > 0 -> {
                                    moveTo(currentIndex - 1)
                                }
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            totalDrag += dragAmount
                        }
                    )
                }
        )

        Text(
            text = item.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = darkText,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = { onSpeak(item.name) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF39796B)
            )
        ) {
            Text(
                text = "🔊  Listen",
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun LargeLearningImage(
    item: LearningItem,
    backgroundColor: Color,
    flipHorizontally: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val bitmap = remember(item.assetPath) {
        item.assetPath?.let { path ->
            runCatching {
                context.assets.open(path).use { input ->
                    BitmapFactory.decodeStream(input)
                }
            }.getOrNull()
        }
    }

    val imageShape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .clip(imageShape)
            .border(1.dp, cardBorder, imageShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = if (flipHorizontally) -1f else 1f
                    },
                contentScale = ContentScale.Fit
            )
        } else {
            Text(
                text = item.emoji,
                fontSize = 120.sp,
                modifier = Modifier.graphicsLayer {
                    scaleX = if (flipHorizontally) -1f else 1f
                }
            )
        }
    }
}
