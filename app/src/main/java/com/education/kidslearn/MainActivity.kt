package com.education.kidslearn

import android.graphics.BitmapFactory
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
    val color: Color,
    val assetPath: String? = null
)

private val categories = listOf(
    LearningCategory("Animals", "🐘", Color(0xFFE3F2FD)),
    LearningCategory("Birds", "🦜", Color(0xFFFFF3E0)),
    LearningCategory("Vehicles", "🚗", Color(0xFFE8F5E9)),
    LearningCategory("Fruits", "🍎", Color(0xFFFFEBEE)),
    LearningCategory("Flowers", "🌻", Color(0xFFFFF9C4)),
    LearningCategory("Clothes", "👕", Color(0xFFEDE7F6)),
    LearningCategory("Insects", "🦋", Color(0xFFF3E5F5)),
    LearningCategory("Food", "🍕", Color(0xFFFFE0B2)),
    LearningCategory("Numbers", "🔢", Color(0xFFE0F7FA)),
    LearningCategory("Shapes", "⭐", Color(0xFFFCE4EC))
)

private val animals = listOf(
    LearningItem("Lion", "🦁", Color(0xFFFFF3E0), "animals/lion.png"),
    LearningItem("Elephant", "🐘", Color(0xFFE3F2FD), "animals/elephant.png"),
    LearningItem("Panda", "🐼", Color(0xFFF5F5F5), "animals/panda.png"),
    LearningItem("Monkey", "🐒", Color(0xFFFFF8E1), "animals/monkey.png"),
    LearningItem("Rabbit", "🐰", Color(0xFFFCE4EC), "animals/rabbit.png"),
    LearningItem("Tiger", "🐯", Color(0xFFFFE0B2), "animals/tiger.png"),
    LearningItem("Dog", "🐶", Color(0xFFFFF3E0), "animals/dog.png"),
    LearningItem("Cat", "🐱", Color(0xFFEDE7F6), "animals/cat.png"),
    LearningItem("Cow", "🐮", Color(0xFFE8F5E9), "animals/cow.png"),
    LearningItem("Horse", "🐴", Color(0xFFFFF8E1), "animals/horse.png"),
    LearningItem("Goat", "🐐", Color(0xFFE0F7FA), "animals/goat.png"),
    LearningItem("Sheep", "🐑", Color(0xFFF3E5F5), "animals/sheep.png"),
    LearningItem("Giraffe", "🦒", Color(0xFFFFF9C4), "animals/giraffe.png"),
    LearningItem("Zebra", "🦓", Color(0xFFF5F5F5), "animals/zebra.png"),
    LearningItem("Bear", "🐻", Color(0xFFFFE0B2), "animals/bear.png"),
    LearningItem("Fox", "🦊", Color(0xFFFFF3E0), "animals/fox.png"),
    LearningItem("Deer", "🦌", Color(0xFFE8F5E9), "animals/deer.png"),
    LearningItem("Wolf", "🐺", Color(0xFFE3F2FD), "animals/wolf.png"),
    LearningItem("Kangaroo", "🦘", Color(0xFFFFF8E1), "animals/kangaroo.png"),
    LearningItem("Hippopotamus", "🦛", Color(0xFFEDE7F6), "animals/hippo.png")
)

private val birds = listOf(
    LearningItem("Parrot", "🦜", Color(0xFFE8F5E9)),
    LearningItem("Peacock", "🦚", Color(0xFFE3F2FD)),
    LearningItem("Owl", "🦉", Color(0xFFFFF3E0)),
    LearningItem("Eagle", "🦅", Color(0xFFEDE7F6)),
    LearningItem("Duck", "🦆", Color(0xFFFFF9C4)),
    LearningItem("Chicken", "🐔", Color(0xFFFFEBEE)),
    LearningItem("Rooster", "🐓", Color(0xFFFFF3E0)),
    LearningItem("Swan", "🦢", Color(0xFFE0F7FA)),
    LearningItem("Flamingo", "🦩", Color(0xFFFCE4EC)),
    LearningItem("Penguin", "🐧", Color(0xFFE3F2FD)),
    LearningItem("Dove", "🕊️", Color(0xFFF5F5F5)),
    LearningItem("Turkey", "🦃", Color(0xFFFFE0B2)),
    LearningItem("Woodpecker", "🐦", Color(0xFFE8F5E9)),
    LearningItem("Kingfisher", "🐦", Color(0xFFE0F7FA)),
    LearningItem("Crow", "🐦‍⬛", Color(0xFFEDE7F6)),
    LearningItem("Sparrow", "🐦", Color(0xFFFFF8E1)),
    LearningItem("Pigeon", "🕊️", Color(0xFFE3F2FD)),
    LearningItem("Hummingbird", "🐦", Color(0xFFF3E5F5)),
    LearningItem("Ostrich", "🪶", Color(0xFFFFF3E0)),
    LearningItem("Crane", "🐦", Color(0xFFE8F5E9))
)

private val categoryItems: Map<String, List<LearningItem>> = mapOf(
    "Animals" to animals,
    "Birds" to birds,

    "Vehicles" to listOf(
        LearningItem("Car", "🚗", Color(0xFFE3F2FD)),
        LearningItem("Bus", "🚌", Color(0xFFFFF9C4)),
        LearningItem("Train", "🚆", Color(0xFFE8F5E9)),
        LearningItem("Airplane", "✈️", Color(0xFFE0F7FA)),
        LearningItem("Bicycle", "🚲", Color(0xFFFCE4EC)),
        LearningItem("Motorcycle", "🏍️", Color(0xFFFFE0B2)),
        LearningItem("Truck", "🚚", Color(0xFFEDE7F6)),
        LearningItem("Boat", "⛵", Color(0xFFE3F2FD)),
        LearningItem("Helicopter", "🚁", Color(0xFFFFF3E0)),
        LearningItem("Tractor", "🚜", Color(0xFFE8F5E9))
    ),

    "Fruits" to listOf(
        LearningItem("Apple", "🍎", Color(0xFFFFEBEE)),
        LearningItem("Banana", "🍌", Color(0xFFFFF9C4)),
        LearningItem("Orange", "🍊", Color(0xFFFFF3E0)),
        LearningItem("Mango", "🥭", Color(0xFFFFE0B2)),
        LearningItem("Grapes", "🍇", Color(0xFFEDE7F6)),
        LearningItem("Watermelon", "🍉", Color(0xFFE8F5E9)),
        LearningItem("Pineapple", "🍍", Color(0xFFFFF9C4)),
        LearningItem("Strawberry", "🍓", Color(0xFFFCE4EC)),
        LearningItem("Pear", "🍐", Color(0xFFE8F5E9)),
        LearningItem("Peach", "🍑", Color(0xFFFFE0B2))
    ),

    "Flowers" to listOf(
        LearningItem("Rose", "🌹", Color(0xFFFFEBEE)),
        LearningItem("Sunflower", "🌻", Color(0xFFFFF9C4)),
        LearningItem("Tulip", "🌷", Color(0xFFFCE4EC)),
        LearningItem("Lotus", "🪷", Color(0xFFF3E5F5)),
        LearningItem("Daisy", "🌼", Color(0xFFFFF9C4)),
        LearningItem("Hibiscus", "🌺", Color(0xFFFFEBEE)),
        LearningItem("Lily", "🪷", Color(0xFFEDE7F6)),
        LearningItem("Jasmine", "🌼", Color(0xFFE8F5E9))
    ),

    "Clothes" to listOf(
        LearningItem("Shirt", "👕", Color(0xFFE3F2FD)),
        LearningItem("Trousers", "👖", Color(0xFFEDE7F6)),
        LearningItem("Dress", "👗", Color(0xFFFCE4EC)),
        LearningItem("Shoes", "👟", Color(0xFFE8F5E9)),
        LearningItem("Hat", "👒", Color(0xFFFFF9C4)),
        LearningItem("Socks", "🧦", Color(0xFFFFE0B2)),
        LearningItem("Jacket", "🧥", Color(0xFFE3F2FD)),
        LearningItem("Skirt", "👗", Color(0xFFF3E5F5))
    ),

    "Insects" to listOf(
        LearningItem("Butterfly", "🦋", Color(0xFFF3E5F5)),
        LearningItem("Bee", "🐝", Color(0xFFFFF9C4)),
        LearningItem("Ant", "🐜", Color(0xFFFFE0B2)),
        LearningItem("Ladybug", "🐞", Color(0xFFFFEBEE)),
        LearningItem("Grasshopper", "🦗", Color(0xFFE8F5E9)),
        LearningItem("Spider", "🕷️", Color(0xFFEDE7F6)),
        LearningItem("Mosquito", "🦟", Color(0xFFE0F7FA)),
        LearningItem("Dragonfly", "🪰", Color(0xFFE3F2FD))
    ),

    "Food" to listOf(
        LearningItem("Pizza", "🍕", Color(0xFFFFE0B2)),
        LearningItem("Burger", "🍔", Color(0xFFFFF3E0)),
        LearningItem("Bread", "🍞", Color(0xFFFFF9C4)),
        LearningItem("Rice", "🍚", Color(0xFFF5F5F5)),
        LearningItem("Egg", "🥚", Color(0xFFFFF8E1)),
        LearningItem("Cheese", "🧀", Color(0xFFFFF9C4)),
        LearningItem("Cake", "🎂", Color(0xFFFCE4EC)),
        LearningItem("Sandwich", "🥪", Color(0xFFE8F5E9))
    ),

    "Numbers" to (1..20).map { number ->
        LearningItem(
            number.toString(),
            number.toString(),
            Color(0xFFE0F7FA)
        )
    },

    "Shapes" to listOf(
        LearningItem("Circle", "⚪", Color(0xFFE3F2FD)),
        LearningItem("Square", "🟦", Color(0xFFE8F5E9)),
        LearningItem("Triangle", "🔺", Color(0xFFFFEBEE)),
        LearningItem("Rectangle", "🟩", Color(0xFFE0F7FA)),
        LearningItem("Star", "⭐", Color(0xFFFFF9C4)),
        LearningItem("Heart", "❤️", Color(0xFFFCE4EC)),
        LearningItem("Diamond", "🔷", Color(0xFFEDE7F6)),
        LearningItem("Oval", "🥚", Color(0xFFFFF3E0))
    )
)

class MainActivity : ComponentActivity() {

    private var textToSpeech: TextToSpeech? = null
    private var speechReady = false
    private var pendingSpeechAction: (() -> Unit)? = null
    private val speechLocale = Locale.US

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeech = TextToSpeech(this) { status ->
            val tts = textToSpeech

            if (status == TextToSpeech.SUCCESS && tts != null) {
                val result = tts.setLanguage(speechLocale)

                speechReady =
                    result != TextToSpeech.LANG_MISSING_DATA &&
                            result != TextToSpeech.LANG_NOT_SUPPORTED

                if (speechReady) {
                    tts.setSpeechRate(0.85f)

                    val pendingAction = pendingSpeechAction
                    pendingSpeechAction = null
                    pendingAction?.invoke()
                }
            }
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFFFBF5)
                ) {
                    KidsLearnApp(
                        onSpeak = { speakWord(it) },
                        onSpell = { spellWord(it) },
                        onStopSpeech = { stopSpeech() }
                    )
                }
            }
        }
    }

    private fun speakWord(word: String) {
        val tts = textToSpeech

        if (!speechReady || tts == null) {
            pendingSpeechAction = { speakWord(word) }
            return
        }

        pendingSpeechAction = null
        tts.stop()

        tts.speak(
            word,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "word_${System.nanoTime()}"
        )
    }

    private fun spellWord(word: String) {
        val tts = textToSpeech

        if (!speechReady || tts == null) {
            pendingSpeechAction = { spellWord(word) }
            return
        }

        pendingSpeechAction = null
        tts.stop()

        if (word.all { it.isDigit() }) {
            tts.speak(
                word,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "number_${System.nanoTime()}"
            )
            return
        }

        val letters = word.uppercase(Locale.US)
            .filter { it in 'A'..'Z' }

        if (letters.isEmpty()) {
            speakWord(word)
            return
        }

        letters.forEachIndexed { index, letter ->
            tts.speak(
                letter.toString(),
                if (index == 0) {
                    TextToSpeech.QUEUE_FLUSH
                } else {
                    TextToSpeech.QUEUE_ADD
                },
                null,
                "letter_${index}_$letter"
            )

            if (index < letters.lastIndex) {
                tts.playSilentUtterance(
                    300L,
                    TextToSpeech.QUEUE_ADD,
                    "pause_$index"
                )
            }
        }

        tts.speak(
            word,
            TextToSpeech.QUEUE_ADD,
            null,
            "complete_word_${System.nanoTime()}"
        )
    }

    private fun stopSpeech() {
        // Cancel any speech request waiting for TTS initialization.
        pendingSpeechAction = null

        // Stop the current utterance and clear the queued letters and word.
        textToSpeech?.stop()
    }

    override fun onDestroy() {
        stopSpeech()
        textToSpeech?.shutdown()
        textToSpeech = null
        super.onDestroy()
    }
}

@Composable
private fun KidsLearnApp(
    onSpeak: (String) -> Unit,
    onSpell: (String) -> Unit,
    onStopSpeech: () -> Unit
) {
    var selectedCategory by remember {
        mutableStateOf<LearningCategory?>(null)
    }

    BackHandler(enabled = selectedCategory != null) {
        onStopSpeech()
        selectedCategory = null
    }

    if (selectedCategory == null) {
        HomeScreen(
            onCategorySelected = { category ->
                onStopSpeech()
                selectedCategory = category
            }
        )
    } else {
        val category = selectedCategory!!

        LearningScreen(
            category = category,
            items = categoryItems[category.name].orEmpty(),
            onBack = {
                onStopSpeech()
                selectedCategory = null
            },
            onSpeak = onSpeak,
            onSpell = onSpell
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    onCategorySelected: (LearningCategory) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kids Learn",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        containerColor = Color(0xFFFFFBF5)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = "What would you like to learn today?",
                fontSize = 19.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF37474F),
                modifier = Modifier.padding(
                    start = 4.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
            )

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
                                onCategorySelected(category)
                            },
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = category.color
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
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
                                fontSize = 54.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = category.name,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF263238)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LearningScreen(
    category: LearningCategory,
    items: List<LearningItem>,
    onBack: () -> Unit,
    onSpeak: (String) -> Unit,
    onSpell: (String) -> Unit
) {
    val shuffledItems = remember(category.name) {
        items.shuffled()
    }

    var currentIndex by remember(category.name) {
        mutableStateOf(0)
    }

    var totalDrag by remember {
        mutableFloatStateOf(0f)
    }

    val item = shuffledItems.getOrNull(currentIndex)

    LaunchedEffect(category.name, item?.name) {
        item?.let { onSpeak(it.name) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = category.name,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Text(
                        text = "‹",
                        fontSize = 38.sp,
                        color = Color(0xFF37474F),
                        modifier = Modifier
                            .clickable { onBack() }
                            .padding(horizontal = 16.dp)
                    )
                }
            )
        },
        containerColor = item?.color ?: Color(0xFFFFFBF5)
    ) { padding ->
        if (item != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Swipe left or right to explore",
                    fontSize = 14.sp,
                    color = Color(0xFF546E7A),
                    modifier = Modifier.padding(
                        top = 4.dp,
                        bottom = 8.dp
                    )
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .pointerInput(category.name, currentIndex) {
                            detectHorizontalDragGestures(
                                onHorizontalDrag = { _, dragAmount ->
                                    totalDrag += dragAmount
                                },
                                onDragEnd = {
                                    if (totalDrag < -60f) {
                                        currentIndex =
                                            (currentIndex + 1) %
                                                    shuffledItems.size
                                    } else if (totalDrag > 60f) {
                                        currentIndex =
                                            if (currentIndex == 0) {
                                                shuffledItems.lastIndex
                                            } else {
                                                currentIndex - 1
                                            }
                                    }

                                    totalDrag = 0f
                                },
                                onDragCancel = {
                                    totalDrag = 0f
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                onSpeak(item.name)
                            },
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.88f)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            ItemPicture(
                                item = item,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = item.name,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF263238),
                                modifier = Modifier
                                    .clickable {
                                        onSpeak(item.name)
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            onSpell(item.name)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Text(
                            text = "Spell ${item.name}",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "${currentIndex + 1} of ${shuffledItems.size}",
                        fontSize = 14.sp,
                        color = Color(0xFF546E7A)
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemPicture(
    item: LearningItem,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val bitmap = remember(item.assetPath) {
        item.assetPath?.let { path ->
            runCatching {
                context.assets.open(path).use { stream ->
                    BitmapFactory.decodeStream(stream)?.asImageBitmap()
                }
            }.getOrNull()
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = item.name,
            modifier = modifier,
            contentScale = ContentScale.Fit
        )
    } else {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.emoji,
                fontSize = 112.sp
            )
        }
    }
}
