package com.education.kidslearn.ui.learning

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.education.kidslearn.canvasColors
import com.education.kidslearn.nextCanvasColor
import com.education.kidslearn.signatureBackground
import com.education.kidslearn.QuizContent
import com.education.kidslearn.ItemPicture
import com.education.kidslearn.R
import com.education.kidslearn.data.LearningCategory
import com.education.kidslearn.data.LearningItem
import kotlinx.coroutines.launch

private fun quizPrompt(name: String): String {
    return if (name.isNotEmpty() && name.all { it.isDigit() }) {
        "Find the number $name"
    } else {
        "Find the $name"
    }
}

private fun numberToWords(value: String): String {
    val number = value.toIntOrNull() ?: return value

    val words = listOf(
        "Zero", "One", "Two", "Three", "Four",
        "five", "six", "seven", "eight", "nine",
        "ten", "eleven", "twelve", "thirteen",
        "fourteen", "fifteen", "sixteen", "seventeen",
        "eighteen", "nineteen", "twenty"
    )

    return words.getOrElse(number) { value }
}

/**
 * Returns a short spoken fact for common learning items.
 * Items without a specific fact use a simple fallback sentence.
 */
private fun informationForItem(name: String): String {
    return when (name.lowercase()) {
        "lion" -> "Lions are big cats. A group of lions is called a pride."
        "elephant" -> "Elephants have trunks that help them smell, drink, and pick things up."
        "panda" -> "Giant pandas eat mostly bamboo."
        "giraffe" -> "Giraffes are the tallest land animals and have very long necks."
        "zebra" -> "Every zebra has a unique pattern of stripes."
        "monkey" -> "Monkeys use their hands to hold food and climb."
        "rabbit" -> "Rabbits have long ears and strong hind legs for jumping."
        "tiger" -> "Tigers are big cats with orange fur and dark stripes."
        "dog" -> "Dogs have a strong sense of smell and can learn many commands."
        "cat" -> "Cats use their whiskers to help sense the space around them."
        "cow" -> "Cows are farm animals that eat grass and other plants."
        "horse" -> "Horses are strong animals that can run quickly."
        "parrot" -> "Many parrots can imitate sounds and have colourful feathers."
        "peacock" -> "A peacock can spread its colourful tail feathers like a fan."
        "owl" -> "Owls have excellent hearing and many are active at night."
        "duck" -> "Ducks have webbed feet that help them swim."
        "penguin" -> "Penguins are birds that cannot fly but are excellent swimmers."
        "car" -> "A car is a road vehicle used to carry people."
        "bus" -> "A bus carries many passengers from one place to another."
        "train" -> "A train travels on railway tracks and can carry people or goods."
        "airplane" -> "An airplane flies through the air using its wings and engines."
        "bicycle" -> "A bicycle has two wheels and is powered by pedalling."
        "motorcycle" -> "A motorcycle is a motor-powered two-wheeled vehicle."
        "truck" -> "A truck is often used to transport goods and heavy loads."
        "boat" -> "A boat travels on water."
        "helicopter" -> "A helicopter uses rotating blades to fly."
        "tractor" -> "A tractor is a powerful vehicle often used on farms."
        else -> "This is a $name."
    }
}

/**
 * Shared action controls for both portrait and landscape layouts.
 */

private fun categoryHasSounds(categoryName: String): Boolean {
    return categoryName.trim().lowercase() in setOf(
        "animals",
        "birds",
        "vehicles",
        "insects"
    )
}

private fun getSoundResource(itemName: String): Int? {
    return when (itemName.trim().lowercase()) {
        "lion" -> R.raw.lion
        "dog" -> R.raw.dog
        else -> null
    }
}

@Composable
private fun LearningActionButtons(
    showSound: Boolean,
    onSpellClick: () -> Unit,
    onSoundClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Spell button: shown in every learning category.
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(onClick = onSpellClick) {
                Text(
                    text = "🔤",
                    fontSize = 28.sp
                )
            }
            Text(
                text = "Spell",
                fontSize = 13.sp
            )
        }

        // Sound button: shown only when this category supports sounds.
        if (showSound) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onSoundClick) {
                    Text(
                        text = "🔊",
                        fontSize = 28.sp
                    )
                }
                Text(
                    text = "Sound",
                    fontSize = 13.sp
                )
            }
        }

        // Info button: shown in every learning category.
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(onClick = onInfoClick) {
                Text(
                    text = "ⓘ",
                    fontSize = 30.sp,
                    color = Color(0xFF1976D2)
                )
            }
            Text(
                text = "Info",
                fontSize = 13.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningScreen(
    category: LearningCategory,
    items: List<LearningItem>,
    quizQuestionCount: Int = 20,
    onBack: () -> Unit,
    onSpeak: (String) -> Unit,
    onSpell: (String) -> Unit,
    onSpellingProgress: (((Int) -> Unit)) -> Unit,
    onPlayPictureSound: (String) -> Unit
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

    // Quiz sessions use exactly 20 distinct items when at least 20 are available.
    // Other learning categories continue to use their full shuffled item list.
    val sessionItems =
        if (category.name == "Quiz") {shuffledItems.take(quizQuestionCount.coerceIn(1, 20))}
        else {shuffledItems}

    val item = sessionItems.getOrNull(currentIndex)

    val spellingWord = if (category.name == "Numbers") {
        numberToWords(item?.name.orEmpty())
    } else {
        item?.name.orEmpty()
    }

    var canvasColor by remember(category.name) {
        mutableStateOf(canvasColors.random())
    }

    var spellingIndex by remember { mutableStateOf(-1) }
    var quizMode by rememberSaveable(category.name) {mutableStateOf(category.name == "Quiz")}
    var quizFeedback by rememberSaveable(category.name) {
        mutableStateOf<String?>(null)
    }
    var quizAnsweredCorrectly by rememberSaveable(category.name) {
        mutableStateOf(false)
    }
    var quizFirstAttemptCorrect by rememberSaveable(category.name) {
        mutableStateOf(true)
    }
    var quizScore by rememberSaveable(category.name) {
        mutableStateOf(0)
    }
    var quizComplete by rememberSaveable(category.name) {
        mutableStateOf(false)
    }

    // Persist which questions have already received their first answer.
    var attemptedQuestionIndices by rememberSaveable(category.name) {
        mutableStateOf(emptyList<Int>())
    }

    val quizOptions = remember(category.name, item?.name, items) {
        if (item == null || items.isEmpty()) {
            emptyList()
        } else {
            (items.filter { it.name != item.name }.shuffled().take(3) + item)
                .shuffled()
        }
    }

    LaunchedEffect(Unit) {
        onSpellingProgress { index ->
            spellingIndex = index
        }
    }

    // Remember the last quiz prompt across orientation changes.
    var lastSpokenQuizItem by rememberSaveable(category.name) {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(category.name, item?.name) {
        canvasScale.snapTo(1f)
        spellingIndex = -1

        // Do not reset quiz answer/feedback state in this effect.
        // Explicit navigation resets the quiz answer state when needed.
        item?.let {
            canvasColor = nextCanvasColor(canvasColor)

            if (quizMode) {
                if (lastSpokenQuizItem != it.name) {
                    lastSpokenQuizItem = it.name
                    onSpeak(quizPrompt(it.name))
                }
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
        containerColor = signatureBackground
    ) { padding ->
        if (item != null) {
            val configuration = LocalConfiguration.current
            val isLandscape =
                configuration.screenWidthDp > configuration.screenHeightDp

            if (quizMode) {
                QuizContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    onHome = onBack,
                    item = item,
                    options = quizOptions,
                    bitmapCache = bitmapCache,
                    isLandscape = isLandscape,
                    feedback = quizFeedback,
                    answeredCorrectly = quizAnsweredCorrectly,
                    questionNumber = currentIndex + 1,
                    totalQuestions = sessionItems.size,
                    score = quizScore,
                    quizComplete = quizComplete,
                    onReplay = {
                        onSpeak(quizPrompt(item.name))
                    },
                    onChoose = { selected ->
                        // Once correct, ignore further taps for this question.
                        if (!quizAnsweredCorrectly) {
                            val correct = selected.name == item.name
                            val hasAnsweredThisQuestion =
                                currentIndex in attemptedQuestionIndices

                            // Only the first attempt can earn a point.
                            if (!hasAnsweredThisQuestion) {
                                attemptedQuestionIndices =
                                    attemptedQuestionIndices + currentIndex

                                if (correct) quizScore += 1
                                quizFirstAttemptCorrect = correct
                            }

                            quizAnsweredCorrectly = correct
                            quizFeedback = if (correct) {"Correct! Great job!"}
                            else {"Not quite. Try again!"}

                            onSpeak(
                                if (correct) "Correct! Great job!"
                                else "Try again"
                            )
                        }
                    },
                    onNext = {
                        if (currentIndex + 1 >= sessionItems.size) {quizComplete = true}
                        else {
                            currentIndex += 1
                            quizFeedback = null
                            quizAnsweredCorrectly = false
                            quizFirstAttemptCorrect = true
                        }
                    },
                    onRestart = {
                        currentIndex = 0
                        quizScore = 0
                        quizFeedback = null
                        quizAnsweredCorrectly = false
                        quizFirstAttemptCorrect = true
                        attemptedQuestionIndices = emptyList()
                        quizComplete = false
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
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFE0D8CC)
                        ),
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
                                        canvasScale.animateTo(
                                            1.045f,
                                            tween(100)
                                        )
                                        canvasScale.animateTo(
                                            1f,
                                            tween(180)
                                        )
                                    }
                                    onSpeak(item.name)
                                }
                                .pointerInput(
                                    category.name,
                                    currentIndex,
                                    swipeThresholdPx
                                ) {
                                    detectHorizontalDragGestures(
                                        onHorizontalDrag = { _, dragAmount ->
                                            totalDrag += dragAmount
                                        },
                                        onDragEnd = {
                                            if (shuffledItems.isNotEmpty()) {
                                                if (totalDrag < -swipeThresholdPx) {
                                                    currentIndex = (currentIndex + 1) % shuffledItems.size
                                                } else if (totalDrag > swipeThresholdPx) {
                                                    currentIndex =
                                                        if (currentIndex == 0) {shuffledItems.lastIndex}
                                                        else {currentIndex - 1}
                                                }
                                            }
                                            totalDrag = 0f
                                        },
                                        onDragCancel = {totalDrag = 0f}
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
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
                                        word = spellingWord,
                                        highlightedIndex = spellingIndex,
                                        modifier = Modifier.padding(
                                            top = 4.dp,
                                            bottom = 4.dp
                                        )
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

                        LearningActionButtons(
                            showSound = categoryHasSounds(category.name),
                            onSpellClick = {
                                spellingIndex = -1
                                onSpell(spellingWord)
                            },
                            onSoundClick = {
                                getSoundResource(item.name)?.let {
                                    onPlayPictureSound(item.name)
                                }
                            },
                            onInfoClick = {onSpeak(informationForItem(item.name))}
                        )

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
                        modifier = Modifier.padding(
                            top = 4.dp,
                            bottom = 8.dp
                        )
                    )

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(28.dp),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFE0D8CC)
                        ),
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
                                        canvasScale.animateTo(
                                            1.045f,
                                            tween(100)
                                        )
                                        canvasScale.animateTo(
                                            1f,
                                            tween(180)
                                        )
                                    }
                                    onSpeak(item.name)
                                }
                                .pointerInput(
                                    category.name,
                                    currentIndex,
                                    swipeThresholdPx
                                ) {
                                    detectHorizontalDragGestures(
                                        onHorizontalDrag = { _, dragAmount ->
                                            totalDrag += dragAmount
                                        },
                                        onDragEnd = {
                                            if (shuffledItems.isNotEmpty()) {
                                                if (totalDrag < -swipeThresholdPx) {
                                                    currentIndex =
                                                        (currentIndex + 1) % shuffledItems.size
                                                } else if (totalDrag > swipeThresholdPx) {
                                                    currentIndex =
                                                        if (currentIndex == 0) {shuffledItems.lastIndex}
                                                        else {currentIndex - 1}
                                                }
                                            }
                                            totalDrag = 0f
                                        },
                                        onDragCancel = {totalDrag = 0f}
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
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
                                        word = spellingWord,
                                        highlightedIndex = spellingIndex,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LearningActionButtons(
                            showSound = categoryHasSounds(category.name),
                            onSpellClick = {
                                spellingIndex = -1
                                onSpell(spellingWord)
                            },
                            onSoundClick = {
                                getSoundResource(item.name)?.let {
                                    onPlayPictureSound(item.name)
                                }
                            },
                            onInfoClick = {onSpeak(informationForItem(item.name))}
                        )

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
