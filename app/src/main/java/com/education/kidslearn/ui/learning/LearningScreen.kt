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
import com.education.kidslearn.data.LearningCategory
import com.education.kidslearn.data.LearningItem
import kotlinx.coroutines.launch
import androidx.compose.runtime.DisposableEffect

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
 * Returns several short facts for a learning item.
 * The Info button chooses randomly from these options.
 */
private fun informationOptionsForItem(name: String): List<String> {
    return when (name.trim().lowercase()) {
        "bear" -> listOf( "Bears have an excellent sense of smell that helps them find food from miles away.", "Some bears sleep all winter long in a deep sleep called hibernation.", "A baby bear is called a cub and stays with its mother for a few years." )
        "cat" -> listOf( "Cats can jump up to six times their height in a single bound.", "Cats spend about half of their waking hours grooming and cleaning themselves.", "A cat uses its whiskers like feelers to see if it can fit through tight spaces." )
        "cow" -> listOf( "Cows have four stomach compartments that help them digest tough grass.", "Cows are social animals and can make special best friends within their herd.", "A cow can smell things that are up to six miles away." )
        "deer" -> listOf( "Male deer grow new antlers every year and shed the old ones in the winter.", "Deer are excellent runners and can jump very high to escape danger.", "Baby deer, called fawns, have white spots that help them hide in the forest." )
        "dog" -> listOf( "Dogs have a sense of smell that is thousands of times stronger than a human's.", "When a dog wags its tail to the right, it usually means they are happy.", "Dogs can understand many human words and body language signals." )
        "elephant" -> listOf( "Elephants are the largest land animals alive on Earth today.", "An elephant uses its long trunk like a hand to breathe, drink, and grab food.", "Elephants can communicate with each other using deep sounds that humans cannot hear." )
        "fox" -> listOf( "Foxes use their big, bushy tails like blankets to keep warm in the winter.", "A fox has amazing hearing and can even hear tiny mice squeaking under the snow.", "Foxes belong to the same animal family as dogs, but they can climb trees like cats." )
        "giraffe" -> listOf( "Giraffes are the tallest mammals on Earth, and even their tongues can be a foot and a half long.", "A giraffe's long neck has the exact same number of bones as a human neck.", "Giraffes only need to sleep for about thirty minutes to two hours every day." )
        "goat" -> listOf( "Goats have rectangular pupils in their eyes that help them see all around themselves.", "Goats are excellent climbers and can balance on very narrow mountain ledges.", "Baby goats are called kids, just like human children." )
        "hippo" -> listOf( "Hippos spend most of their day resting in water to keep their skin cool and moist.", "Despite being very large and heavy, hippos can run faster than humans on land.", "A hippo's sweat looks bright pink and acts like a natural sunscreen." )
        "horse" -> listOf( "Horses can sleep both while standing up and while lying down.", "Horses have bigger eyes than any other mammal that lives on land.", "A horse's teeth take up more space in its head than its brain does." )
        "kangaroo" -> listOf( "Kangaroos use their large, powerful hind legs to hop instead of walk.", "Mother kangaroos carry their tiny babies inside a built-in belly pocket called a pouch.", "A kangaroo uses its big, strong tail like a third leg to help it balance." )
        "lion" -> listOf( "Lions are big cats. A group of lions is called a pride.", "Lions use their powerful paws and sharp claws to catch prey.", "A lion's roar can be heard from far away." )
        "monkey" -> listOf( "Many monkeys use their long tails like an extra hand to swing from tree branches.", "Monkeys express affection and stay clean by grooming each other's fur.", "Monkeys are highly intelligent animals that can learn to use simple tools." )
        "panda" -> listOf( "Giant pandas spend up to twelve hours every day munching on green bamboo.", "Pandas have a special wrist bone that works like a thumb to help them hold bamboo sticks.", "Baby pandas are born completely pink, blind, and are about the size of a stick of butter." )
        "rabbit" -> listOf( "Rabbits have long ears that can turn in different directions to listen for danger.", "A happy rabbit will perform a joyful twist in the air called a binky.", "A rabbit's teeth never stop growing, so they need to chew on grass to wear them down." )
        "sheep" -> listOf( "Sheep are covered in thick, fluffy wool that humans use to make warm clothes.", "Sheep have great memories and can recognize the faces of their friends for years.", "When sheep are sick, they can find and eat specific plants that make them feel better." )
        "tiger" -> listOf( "Tigers are the biggest species of cats in the whole world.", "Every tiger has a completely unique pattern of stripes, just like human fingerprints.", "Unlike most pet cats, tigers love the water and are excellent swimmers." )
        "wolf" -> listOf( "Wolves live and hunt together in close-knit family groups called packs.", "Wolves howl to talk to their pack members and warn other wolves to stay away.", "A wolf can walk and run for many miles a day without getting tired." )
        "zebra" -> listOf( "Zebras have black and white stripes that help confuse biting insects and predators.", "Every zebra has a unique pattern of stripes that is different from any other zebra.", "Zebras sleep standing up so they can quickly run away if danger arrives." )

        else -> listOf(
            "This is a $name.",
            "Can you say the word $name?",
            "Look carefully at the picture of the $name."
        )
    }
}

/**
 * Shared action controls for both portrait and landscape layouts.
 */
private fun categoryHasSounds(categoryName: String): Boolean {
    return categoryName.trim().equals("Animals", ignoreCase = true)
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(onClick = onSpellClick) {
                Text(text = "🔤", fontSize = 28.sp)
            }
            Text(text = "Spell", fontSize = 13.sp)
        }

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
    onSpellingProgress: (((Int) -> Unit)?) -> Unit,
    onPlayPictureSound: (String) -> Unit,
    onStopPictureSound: () -> Unit
) {
    // Numbers always retain their original sequential order.
    // Every other category keeps its shuffled order.
    val savedItemOrder = rememberSaveable(category.name) {
        mutableStateOf(
            if (category.name == "Numbers") {
                items.map { it.name }
            } else {
                items.shuffled().map { it.name }
            }
        )
    }

    val orderedItems = remember(category.name, savedItemOrder.value, items) {
        if (category.name == "Numbers") {
            items
        } else {
            savedItemOrder.value.mapNotNull { savedName ->
                items.firstOrNull { it.name == savedName }
            }
        }
    }

    var currentIndex by rememberSaveable(category.name) {
        mutableStateOf(0)
    }

    // Track swipe distance without triggering recomposition on every drag event.
    val totalDrag = remember(category.name) { floatArrayOf(0f) }

    val canvasScale = remember { Animatable(1f) }
    val animationScope = rememberCoroutineScope()
    val canvasInteractionSource = remember { MutableInteractionSource() }
    val bitmapCache = remember { mutableMapOf<String, ImageBitmap>() }
    val swipeThresholdPx = with(LocalDensity.current) { 60.dp.toPx() }

    val sessionItems =
        if (category.name == "Quiz") {
            orderedItems.take(quizQuestionCount.coerceIn(1, 20))
        } else {
            orderedItems
        }

    val item = sessionItems.getOrNull(currentIndex)

    val spellingWord = if (category.name == "Numbers") {
        numberToWords(item?.name.orEmpty())
    } else {
        item?.name.orEmpty()
    }

    var canvasColor by remember(category.name) {
        mutableStateOf(canvasColors.random())
    }

    var lastInfoFactByItem by remember(category.name) {
        mutableStateOf<Map<String, String>>(emptyMap())
    }

    fun speakRandomInfo(itemName: String) {
        val facts = informationOptionsForItem(itemName)
        val previousFact = lastInfoFactByItem[itemName]

        val choices = facts.filter { it != previousFact }
        val selectedFact = (choices.ifEmpty { facts }).random()

        lastInfoFactByItem =
            lastInfoFactByItem + (itemName to selectedFact)

        onSpeak(selectedFact)
    }

    var spellingIndex by remember { mutableStateOf(-1) }

    var quizMode by rememberSaveable(category.name) {
        mutableStateOf(category.name == "Quiz")
    }

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

    DisposableEffect(Unit) {
        // Register the spelling progress callback while this screen is active.
        onSpellingProgress { index ->
            spellingIndex = index
        }

        // Unregister it when this screen leaves the composition.
        onDispose {
            onSpellingProgress(null)
            onStopPictureSound()
        }
    }

    var lastSpokenQuizItem by rememberSaveable(category.name) {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(category.name, item?.name) {
        onStopPictureSound()
        canvasScale.snapTo(1f)
        spellingIndex = -1

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
                        if (!quizAnsweredCorrectly) {
                            val correct = selected.name == item.name
                            val hasAnsweredThisQuestion =
                                currentIndex in attemptedQuestionIndices

                            if (!hasAnsweredThisQuestion) {
                                attemptedQuestionIndices =
                                    attemptedQuestionIndices + currentIndex

                                if (correct) quizScore += 1
                                quizFirstAttemptCorrect = correct
                            }

                            quizAnsweredCorrectly = correct
                            quizFeedback = if (correct) {
                                "Correct! Great job!"
                            } else {
                                "Not quite. Try again!"
                            }

                            onSpeak(
                                if (correct) "Correct! Great job!"
                                else "Try again"
                            )
                        }
                    },
                    onNext = {
                        if (currentIndex + 1 >= sessionItems.size) {
                            quizComplete = true
                        } else {
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
                                    onStopPictureSound()
                                    onSpeak(item.name)
                                }
                                .pointerInput(
                                    category.name,
                                    currentIndex,
                                    swipeThresholdPx
                                ) {
                                    detectHorizontalDragGestures(
                                        onHorizontalDrag = { _, dragAmount ->
                                            totalDrag[0] += dragAmount
                                        },
                                        onDragEnd = {
                                            if (sessionItems.isNotEmpty()) {
                                                if (totalDrag[0] < -swipeThresholdPx) {
                                                    currentIndex =
                                                        (currentIndex + 1) % sessionItems.size
                                                } else if (totalDrag[0] > swipeThresholdPx) {
                                                    currentIndex =
                                                        if (currentIndex == 0) {
                                                            sessionItems.lastIndex
                                                        } else {
                                                            currentIndex - 1
                                                        }
                                                }
                                            }
                                            totalDrag[0] = 0f
                                        },
                                        onDragCancel = {
                                            totalDrag[0] = 0f
                                        }
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
                                onStopPictureSound()
                                spellingIndex = -1
                                onSpell(spellingWord)
                            },
                            onSoundClick = {onPlayPictureSound(item.name)},
                            onInfoClick = {
                                onStopPictureSound()
                                speakRandomInfo(item.name)
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "${currentIndex + 1} of ${sessionItems.size}",
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
                                    onStopPictureSound()
                                    onSpeak(item.name)
                                }
                                .pointerInput(
                                    category.name,
                                    currentIndex,
                                    swipeThresholdPx
                                ) {
                                    detectHorizontalDragGestures(
                                        onHorizontalDrag = { _, dragAmount ->
                                            totalDrag[0] += dragAmount
                                        },
                                        onDragEnd = {
                                            if (sessionItems.isNotEmpty()) {
                                                if (totalDrag[0] < -swipeThresholdPx) {
                                                    currentIndex =
                                                        (currentIndex + 1) % sessionItems.size
                                                } else if (totalDrag[0] > swipeThresholdPx) {
                                                    currentIndex =
                                                        if (currentIndex == 0) {
                                                            sessionItems.lastIndex
                                                        } else {
                                                            currentIndex - 1
                                                        }
                                                }
                                            }
                                            totalDrag[0] = 0f
                                        },
                                        onDragCancel = {
                                            totalDrag[0] = 0f
                                        }
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
                                onStopPictureSound()
                                spellingIndex = -1
                                onSpell(spellingWord)
                            },
                            onSoundClick = {onPlayPictureSound(item.name)},
                            onInfoClick = {
                                onStopPictureSound()
                                speakRandomInfo(item.name)
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "${currentIndex + 1} of ${sessionItems.size}",
                            fontSize = 14.sp,
                            color = Color(0xFF546E7A)
                        )
                    }
                }
            }
        }
    }
}
