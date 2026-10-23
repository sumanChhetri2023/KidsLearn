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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.education.kidslearn.data.LearningCategory
import com.education.kidslearn.data.LearningItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningScreen(
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

