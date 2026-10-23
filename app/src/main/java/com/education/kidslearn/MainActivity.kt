package com.education.kidslearn

import android.graphics.BitmapFactory
import android.util.Log
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlinx.coroutines.launch

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

private val signatureBackground = Color(0xFFFFFBF5)

private val canvasColors = listOf(
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

private fun nextCanvasColor(current: Color): Color {
    val alternatives = canvasColors.filter { it != current }
    return alternatives.random()
}

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
    LearningCategory("Shapes", "⭐", Color(0xFFFCE4EC)),
    LearningCategory("Quiz", "🎯", Color(0xFFE0F2F1))
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
    LearningItem("Parrot", "🦜", Color(0xFFE8F5E9), assetPath = "birds/parrot.png"),
    LearningItem("Peacock", "🦚", Color(0xFFE3F2FD), assetPath = "birds/peacock.png"),
    LearningItem("Owl", "🦉", Color(0xFFFFF3E0), assetPath = "birds/owl.png"),
    LearningItem("Eagle", "🦅", Color(0xFFEDE7F6), assetPath = "birds/eagle.png"),
    LearningItem("Duck", "🦆", Color(0xFFFFF9C4), assetPath = "birds/duck.png"),
    LearningItem("Chicken", "🐔", Color(0xFFFFEBEE), assetPath = "birds/chicken.png"),
    LearningItem("Rooster", "🐓", Color(0xFFFFF3E0), assetPath = "birds/rooster.png"),
    LearningItem("Swan", "🦢", Color(0xFFE0F7FA), assetPath = "birds/swan.png"),
    LearningItem("Flamingo", "🦩", Color(0xFFFCE4EC), assetPath = "birds/flamingo.png"),
    LearningItem("Penguin", "🐧", Color(0xFFE3F2FD), assetPath = "birds/penguin.png"),
    LearningItem("Dove", "🕊️", Color(0xFFF5F5F5), assetPath = "birds/dove.png"),
    LearningItem("Turkey", "🦃", Color(0xFFFFE0B2), assetPath = "birds/turkey.png"),
    LearningItem("Woodpecker", "🐦", Color(0xFFE8F5E9), assetPath = "birds/woodpecker.png"),
    LearningItem("Kingfisher", "🐦", Color(0xFFE0F7FA), assetPath = "birds/kingfisher.png"),
    LearningItem("Crow", "🐦‍⬛", Color(0xFFEDE7F6), assetPath = "birds/crow.png"),
    LearningItem("Sparrow", "🐦", Color(0xFFFFF8E1), assetPath = "birds/sparrow.png"),
    LearningItem("Pigeon", "🕊️", Color(0xFFE3F2FD), assetPath = "birds/pigeon.png"),
    LearningItem("Hummingbird", "🐦", Color(0xFFF3E5F5), assetPath = "birds/hummingbird.png"),
    LearningItem("Ostrich", "🪶", Color(0xFFFFF3E0), assetPath = "birds/ostrich.png"),
    LearningItem("Crane", "🐦", Color(0xFFE8F5E9), assetPath = "birds/crane.png")
)

private val categoryItems: Map<String, List<LearningItem>> = mapOf(
    "Animals" to animals,
    "Birds" to birds,
    "Quiz" to (animals + birds + listOf(
        LearningItem("Car", "🚗", Color(0xFFE3F2FD), "vehicles/car.png"),
        LearningItem("Bus", "🚌", Color(0xFFFFF9C4), "vehicles/bus.png"),
        LearningItem("Train", "🚆", Color(0xFFE8F5E9), "vehicles/train.png"),
        LearningItem("Airplane", "✈️", Color(0xFFE0F7FA), "vehicles/airplane.png"),
        LearningItem("Bicycle", "🚲", Color(0xFFFCE4EC), "vehicles/bicycle.png"),
        LearningItem("Motorcycle", "🏍️", Color(0xFFFFE0B2), "vehicles/motorcycle.png"),
        LearningItem("Truck", "🚚", Color(0xFFEDE7F6), "vehicles/truck.png"),
        LearningItem("Boat", "⛵", Color(0xFFE0F7FA), "vehicles/boat.png"),
        LearningItem("Helicopter", "🚁", Color(0xFFFFF3E0), "vehicles/helicopter.png"),
        LearningItem("Tractor", "🚜", Color(0xFFE8F5E9), "vehicles/tractor.png")
    )),

    "Vehicles" to listOf(
        LearningItem("Car", "🚗", Color(0xFFE3F2FD), assetPath = "vehicles/car.png"),
        LearningItem("Bus", "🚌", Color(0xFFFFF9C4), assetPath = "vehicles/bus.png"),
        LearningItem("Train", "🚆", Color(0xFFE8F5E9), assetPath = "vehicles/train.png"),
        LearningItem("Airplane", "✈️", Color(0xFFE0F7FA), assetPath = "vehicles/airplane.png"),
        LearningItem("Bicycle", "🚲", Color(0xFFFCE4EC), assetPath = "vehicles/bicycle.png"),
        LearningItem("Motorcycle", "🏍️", Color(0xFFFFE0B2), assetPath = "vehicles/motorcycle.png"),
        LearningItem("Truck", "🚚", Color(0xFFEDE7F6), assetPath = "vehicles/truck.png"),
        LearningItem("Boat", "⛵", Color(0xFFE3F2FD), assetPath = "vehicles/boat.png"),
        LearningItem("Helicopter", "🚁", Color(0xFFFFF3E0), assetPath = "vehicles/helicopter.png"),
        LearningItem("Tractor", "🚜", Color(0xFFE8F5E9), assetPath = "vehicles/tractor.png")
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


private fun lettersForSpelling(word: String): List<Char> =
    word.uppercase(Locale.US).filter { it in 'A'..'Z' }.toList()

@Composable
private fun SpellingLetters(
    word: String,
    highlightedIndex: Int,
    modifier: Modifier = Modifier
) {
    val letters = lettersForSpelling(word)
    if (letters.isEmpty()) return

    // Short words keep natural-sized letter cells. Long words compress each cell
    // only as much as needed to fit on one line within the canvas width.
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val preferredCellWidth = 32.dp
        val letterGap = 2.dp
        val availableForCells = (maxWidth - letterGap * (letters.size - 1)).coerceAtLeast(1.dp)
        val cellWidth = minOf(preferredCellWidth, availableForCells / letters.size)
        val rowWidth = cellWidth * letters.size + letterGap * (letters.size - 1)

        Row(
            modifier = Modifier.width(rowWidth),
            horizontalArrangement = Arrangement.spacedBy(letterGap),
            verticalAlignment = Alignment.CenterVertically
        ) {
            letters.forEachIndexed { index, letter ->
                Surface(
                    modifier = Modifier.width(cellWidth),
                    shape = RoundedCornerShape(6.dp),
                    color = if (index == highlightedIndex) Color(0xFFFFD54F) else Color.White.copy(alpha = 0.75f),
                    border = BorderStroke(
                        width = if (index == highlightedIndex) 2.dp else 1.dp,
                        color = if (index == highlightedIndex) Color(0xFFFF8F00) else Color(0xFFD7CCC8)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = letter.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF263238),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EducationalVisual(
    item: LearningItem,
    modifier: Modifier = Modifier
) {
    if (item.name.all { it.isDigit() }) {
        val number = item.name.toIntOrNull() ?: 0
        Column(
            modifier = modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.name,
                fontSize = 76.sp,
                lineHeight = 80.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF263238)
            )
            val dotCount = number.coerceIn(0, 20)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                (0 until dotCount step 5).forEach { start ->
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        (start until minOf(start + 5, dotCount)).forEach { index ->
                            Box(
                                modifier = Modifier
                                    .width(13.dp)
                                    .height(13.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Surface(
                                    modifier = Modifier.fillMaxSize(),
                                    shape = RoundedCornerShape(50),
                                    color = listOf(
                                        Color(0xFFEF5350), Color(0xFF42A5F5),
                                        Color(0xFF66BB6A), Color(0xFFFFCA28),
                                        Color(0xFFAB47BC)
                                    )[index % 5]
                                ) {}
                            }
                        }
                    }
                }
            }
        }
    } else if (item.name in setOf("Circle", "Square", "Triangle", "Rectangle", "Star", "Heart", "Diamond", "Oval")) {
        Canvas(modifier = modifier.padding(24.dp)) {
            val shapeColor = Color(0xFF42A5F5)
            val w = size.width
            val h = size.height
            val side = minOf(w, h) * 0.68f
            val left = (w - side) / 2f
            val top = (h - side) / 2f
            when (item.name) {
                "Circle" -> drawCircle(shapeColor, radius = side / 2f)
                "Square" -> drawRect(shapeColor, topLeft = Offset(left, top), size = androidx.compose.ui.geometry.Size(side, side))
                "Rectangle" -> {
                    val rectW = w * 0.82f
                    val rectH = h * 0.48f
                    drawRect(
                        shapeColor,
                        topLeft = Offset((w - rectW) / 2f, (h - rectH) / 2f),
                        size = androidx.compose.ui.geometry.Size(rectW, rectH)
                    )
                }
                "Triangle" -> {
                    val path = Path().apply {
                        moveTo(w / 2f, h * 0.12f)
                        lineTo(w * 0.88f, h * 0.84f)
                        lineTo(w * 0.12f, h * 0.84f)
                        close()
                    }
                    drawPath(path, Color(0xFFEF5350))
                }
                "Diamond" -> {
                    val path = Path().apply {
                        moveTo(w / 2f, h * 0.08f)
                        lineTo(w * 0.9f, h / 2f)
                        lineTo(w / 2f, h * 0.92f)
                        lineTo(w * 0.1f, h / 2f)
                        close()
                    }
                    drawPath(path, Color(0xFFAB47BC))
                }
                "Oval" -> drawOval(
                    color = Color(0xFFFFA726),
                    topLeft = Offset(w * 0.12f, h * 0.28f),
                    size = androidx.compose.ui.geometry.Size(w * 0.76f, h * 0.44f)
                )
                "Star" -> {
                    val path = Path()
                    val cx = w / 2f
                    val cy = h / 2f
                    val outer = minOf(w, h) * 0.44f
                    val inner = outer * 0.43f
                    for (i in 0 until 10) {
                        val angle = (-90.0 + i * 36.0) * Math.PI / 180.0
                        val radius = if (i % 2 == 0) outer else inner
                        val x = cx + kotlin.math.cos(angle).toFloat() * radius
                        val y = cy + kotlin.math.sin(angle).toFloat() * radius
                        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }
                    path.close()
                    drawPath(path, Color(0xFFFFCA28))
                }
                "Heart" -> {
                    val path = Path().apply {
                        moveTo(w * 0.5f, h * 0.82f)
                        cubicTo(w * 0.38f, h * 0.7f, w * 0.1f, h * 0.5f, w * 0.14f, h * 0.3f)
                        cubicTo(w * 0.18f, h * 0.08f, w * 0.42f, h * 0.12f, w * 0.5f, h * 0.3f)
                        cubicTo(w * 0.58f, h * 0.12f, w * 0.82f, h * 0.08f, w * 0.86f, h * 0.3f)
                        cubicTo(w * 0.9f, h * 0.5f, w * 0.62f, h * 0.7f, w * 0.5f, h * 0.82f)
                        close()
                    }
                    drawPath(path, Color(0xFFEF5350))
                }
            }
        }
    } else {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(text = item.emoji, fontSize = 112.sp)
        }
    }
}

class MainActivity : ComponentActivity() {

    private var textToSpeech: TextToSpeech? = null
    private var speechReady = false
    private var pendingSpeechAction: (() -> Unit)? = null
    private var spellingProgressCallback: ((Int) -> Unit)? = null
    private var spellingSession = 0L
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
                    tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            val parts = utteranceId?.split(":") ?: return
                            if (parts.size == 3 && parts[0] == "spell" && parts[1].toLongOrNull() == spellingSession) {
                                val index = parts[2].toIntOrNull() ?: return
                                runOnUiThread { spellingProgressCallback?.invoke(index) }
                            }
                        }
                        override fun onDone(utteranceId: String?) {
                            val parts = utteranceId?.split(":") ?: return
                            if (parts.size >= 2 && parts[0] == "spellDone" && parts[1].toLongOrNull() == spellingSession) {
                                runOnUiThread { spellingProgressCallback?.invoke(-1) }
                            }
                        }
                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) {
                            runOnUiThread { spellingProgressCallback?.invoke(-1) }
                        }
                    })

                    val pendingAction = pendingSpeechAction
                    pendingSpeechAction = null
                    pendingAction?.invoke()
                } else {
                    // Do not retain requests if the selected speech language is unavailable.
                    pendingSpeechAction = null
                }
            } else {
                pendingSpeechAction = null
            }
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = signatureBackground
                ) {
                    KidsLearnApp(
                        onSpeak = { speakWord(it) },
                        onSpell = { spellWord(it) },
                        onStopSpeech = { stopSpeech() },
                        onSpellingProgress = { spellingProgressCallback = it }
                    )
                }
            }
        }
    }

    override fun onStop() {
        stopSpeech()
        super.onStop()
    }

    private fun speakWord(word: String) {
        if (word.isBlank()) return

        val tts = textToSpeech

        if (!speechReady || tts == null) {
            pendingSpeechAction = { speakWord(word) }
            return
        }

        pendingSpeechAction = null
        spellingSession += 1
        spellingProgressCallback?.invoke(-1)
        tts.stop()

        tts.speak(
            word,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "word_${System.nanoTime()}"
        )
    }

    private fun spellWord(word: String) {
        if (word.isBlank()) return

        val tts = textToSpeech

        if (!speechReady || tts == null) {
            pendingSpeechAction = { spellWord(word) }
            return
        }

        pendingSpeechAction = null
        tts.stop()
        spellingSession += 1
        val thisSession = spellingSession
        spellingProgressCallback?.invoke(-1)

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
                if (index == 0) {TextToSpeech.QUEUE_FLUSH} else {TextToSpeech.QUEUE_ADD},
                null,
                "spell:$thisSession:$index"
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
            "spellDone:$thisSession"
        )
    }

    private fun stopSpeech() {
        // Cancel any speech request waiting for TTS initialization.
        pendingSpeechAction = null

        // Stop the current utterance and clear the queued letters and word.
        spellingSession += 1
        spellingProgressCallback?.invoke(-1)
        textToSpeech?.stop()
    }

    override fun onDestroy() {
        stopSpeech()
        textToSpeech?.shutdown()
        textToSpeech = null
        spellingProgressCallback = null
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
        val category = selectedCategory!!

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
        containerColor = signatureBackground
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
    onSpell: (String) -> Unit,
    onSpellingProgress: (((Int) -> Unit)) -> Unit
) {
    // Save the shuffled item order across configuration changes such as rotation.
    // Store item names because rememberSaveable can reliably restore a list of strings.
    val savedItemOrder = rememberSaveable(category.name) {
        mutableStateOf(items.shuffled().map { it.name })
    }
    val shuffledItems = remember(category.name, savedItemOrder.value, items) {
        savedItemOrder.value.mapNotNull { savedName ->
            items.firstOrNull { it.name == savedName }
        }
    }

    // Preserve the selected position when the activity is recreated by rotation.
    var currentIndex by rememberSaveable(category.name) {
        mutableStateOf(0)
    }

    var totalDrag by remember {
        mutableFloatStateOf(0f)
    }

    val canvasScale = remember { Animatable(1f) }
    val animationScope = rememberCoroutineScope()
    val canvasInteractionSource = remember { MutableInteractionSource() }
    val bitmapCache = remember { mutableMapOf<String, ImageBitmap>() }
    val swipeThresholdPx = with(LocalDensity.current) { 60.dp.toPx() }

    val item = shuffledItems.getOrNull(currentIndex)
    var canvasColor by remember(category.name) {
        mutableStateOf(canvasColors.random())
    }
    var spellingIndex by remember { mutableStateOf(-1) }
    var quizMode by rememberSaveable(category.name) { mutableStateOf(category.name == "Quiz") }
    var quizFeedback by rememberSaveable(category.name) { mutableStateOf<String?>(null) }
    var quizAnsweredCorrectly by rememberSaveable(category.name) { mutableStateOf(false) }

    val quizOptions = remember(category.name, item?.name, items) {
        if (item == null || items.isEmpty()) emptyList()
        else (items.filter { it.name != item.name }.shuffled().take(2) + item).shuffled()
    }

    LaunchedEffect(Unit) {
        onSpellingProgress { index -> spellingIndex = index }
    }

    LaunchedEffect(category.name, item?.name) {
        canvasScale.snapTo(1f)
        spellingIndex = -1
        quizFeedback = null
        quizAnsweredCorrectly = false
        item?.let {
            canvasColor = nextCanvasColor(canvasColor)
            if (quizMode) {
                onSpeak("Find the ${it.name}")
            } else {
                onSpeak(it.name)
            }
        }
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
                    IconButton(onClick = onBack) {
                        Text(
                            text = "‹",
                            fontSize = 38.sp,
                            color = Color(0xFF37474F)
                        )
                    }
                },
                actions = {}
            )
        },
        // Keep the learning screen body consistent with the home screen signature background.
        // Only the rounded image canvas changes color randomly.
        containerColor = signatureBackground
    ) { padding ->
        if (item != null) {
            val configuration = LocalConfiguration.current
            val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp

            if (quizMode) {
                QuizContent(
                    item = item,
                    options = quizOptions,
                    bitmapCache = bitmapCache,
                    isLandscape = isLandscape,
                    feedback = quizFeedback,
                    answeredCorrectly = quizAnsweredCorrectly,
                    onReplay = { onSpeak("Find the ${item.name}") },
                    onChoose = { selected ->
                        val correct = selected.name == item.name
                        quizAnsweredCorrectly = correct
                        quizFeedback = if (correct) "Correct! Great job!" else "Not quite. Try again!"
                        onSpeak(if (correct) "Correct! Great job!" else "Try again")
                    },
                    onNext = {
                        currentIndex = (currentIndex + 1) % shuffledItems.size
                        quizFeedback = null
                        quizAnsweredCorrectly = false
                    }
                )
            } else if (isLandscape) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        shape = RoundedCornerShape(28.dp),
                        border = BorderStroke(1.dp, Color(0xFFE0D8CC)),
                        colors = CardDefaults.cardColors(containerColor = canvasColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(
                                    interactionSource = canvasInteractionSource,
                                    indication = null
                                ) {
                                    animationScope.launch {
                                        canvasScale.animateTo(1.045f, tween(100))
                                        canvasScale.animateTo(1f, tween(180))
                                    }
                                    onSpeak(item.name)
                                }
                                .pointerInput(category.name, currentIndex, swipeThresholdPx) {
                                    detectHorizontalDragGestures(
                                        onHorizontalDrag = { _, dragAmount -> totalDrag += dragAmount },
                                        onDragEnd = {
                                            if (totalDrag < -swipeThresholdPx) {
                                                currentIndex = (currentIndex + 1) % shuffledItems.size
                                            } else if (totalDrag > swipeThresholdPx) {
                                                currentIndex = if (currentIndex == 0) shuffledItems.lastIndex else currentIndex - 1
                                            }
                                            totalDrag = 0f
                                        },
                                        onDragCancel = { totalDrag = 0f }
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                ItemPicture(
                                    item = item,
                                    bitmapCache = bitmapCache,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    animationScale = canvasScale.value
                                )
                                Text(
                                    text = item.name,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF263238),
                                    modifier = Modifier.padding(4.dp)
                                )
                                if (spellingIndex >= 0) {
                                    SpellingLetters(
                                        word = item.name,
                                        highlightedIndex = spellingIndex,
                                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(0.42f)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Swipe left or right to explore",
                            fontSize = 14.sp,
                            color = Color(0xFF546E7A),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Button(
                            onClick = {
                                spellingIndex = -1
                                onSpell(item.name)
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text(
                                text = "Spell ${item.name}",
                                fontSize = 18.sp,
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
            } else {
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
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                    )

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(28.dp),
                        border = BorderStroke(1.dp, Color(0xFFE0D8CC)),
                        colors = CardDefaults.cardColors(containerColor = canvasColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(
                                    interactionSource = canvasInteractionSource,
                                    indication = null
                                ) {
                                    animationScope.launch {
                                        canvasScale.animateTo(1.045f, tween(100))
                                        canvasScale.animateTo(1f, tween(180))
                                    }
                                    onSpeak(item.name)
                                }
                                .pointerInput(category.name, currentIndex, swipeThresholdPx) {
                                    detectHorizontalDragGestures(
                                        onHorizontalDrag = { _, dragAmount -> totalDrag += dragAmount },
                                        onDragEnd = {
                                            if (totalDrag < -swipeThresholdPx) {
                                                currentIndex = (currentIndex + 1) % shuffledItems.size
                                            } else if (totalDrag > swipeThresholdPx) {
                                                currentIndex = if (currentIndex == 0) shuffledItems.lastIndex else currentIndex - 1
                                            }
                                            totalDrag = 0f
                                        },
                                        onDragCancel = { totalDrag = 0f }
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                ItemPicture(
                                    item = item,
                                    bitmapCache = bitmapCache,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    animationScale = canvasScale.value
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = item.name,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF263238),
                                    modifier = Modifier.padding(8.dp)
                                )
                                if (spellingIndex >= 0) {
                                    SpellingLetters(
                                        word = item.name,
                                        highlightedIndex = spellingIndex,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                spellingIndex = -1
                                onSpell(item.name)
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
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
}


@Composable
private fun QuizContent(
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

@Composable
private fun ItemPicture(
    item: LearningItem,
    bitmapCache: MutableMap<String, ImageBitmap>,
    modifier: Modifier = Modifier,
    animationScale: Float = 1f
) {
    val context = LocalContext.current

    // Randomize horizontal flipping once whenever the displayed item changes.
    val flipHorizontally = remember(item.name) {
        kotlin.random.Random.nextBoolean()
    }

    val bitmap = remember(item.assetPath, item.name) {
        item.assetPath?.let { path ->
            bitmapCache[path] ?: run {
                // Preferred location: app/src/main/assets/<category>/<file>.png
                val fromAssets = runCatching {
                    context.assets.open(path).use { stream ->
                        BitmapFactory.decodeStream(stream)?.asImageBitmap()
                    }
                }.onFailure { error ->
                    Log.d("KidsLearn", "Asset lookup failed for $path; trying drawable resources.", error)
                }.getOrNull()

                // Fallback location: app/src/main/res/drawable/<file>.png
                val resourceName = path.substringAfterLast('/')
                    .substringBeforeLast('.')
                    .lowercase(Locale.US)
                    .replace('-', '_')
                val resourceId = context.resources.getIdentifier(
                    resourceName,
                    "drawable",
                    context.packageName
                )
                val fromDrawable = if (fromAssets == null && resourceId != 0) {
                    runCatching {
                        BitmapFactory.decodeResource(context.resources, resourceId)?.asImageBitmap()
                    }.onFailure { error ->
                        Log.w("KidsLearn", "Unable to decode drawable resource: $resourceName", error)
                    }.getOrNull()
                } else null

                (fromAssets ?: fromDrawable)?.also { decodedBitmap ->
                    bitmapCache[path] = decodedBitmap
                } ?: run {
                    Log.w(
                        "KidsLearn",
                        "Image not found for ${item.name}. Checked assets/$path and drawable/$resourceName."
                    )
                    null
                }
            }
        }
    }

    if (item.name.all { it.isDigit() } ||
        item.name in setOf("Circle", "Square", "Triangle", "Rectangle", "Star", "Heart", "Diamond", "Oval")
    ) {
        EducationalVisual(item = item, modifier = modifier.scale(animationScale))
    } else if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = item.name,
            modifier = modifier.scale(
                scaleX = (if (flipHorizontally) -1f else 1f) * animationScale,
                scaleY = animationScale
            ),
            contentScale = ContentScale.Fit
        )
    } else {
        Box(
            modifier = modifier.scale(animationScale),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.emoji,
                fontSize = 112.sp
            )
        }
    }
}
