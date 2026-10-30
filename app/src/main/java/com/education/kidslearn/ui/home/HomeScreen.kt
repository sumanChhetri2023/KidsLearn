package com.education.kidslearn.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.education.kidslearn.R
import com.education.kidslearn.data.LearningCategory
import com.education.kidslearn.data.categories
import java.time.LocalTime

private val homeBackground = Color(0xFFFFFBF5)
private val headingColor = Color(0xFF343452)
private val subtitleColor = Color(0xFF77758D)
private val promptBackground = Color(0xFFEDE7FF)

private fun greetingForHour(hour: Int): Pair<String, String> {
    return when (hour) {
        in 5..11 -> "Good morning! ☀️" to "Ready for some fun learning?"
        in 12..16 -> "Good afternoon! 🌤️" to "Ready for some fun learning?"
        in 17..20 -> "Good evening! 🌙" to "Let's discover something new!"
        else -> "Time to wind down! 🌟" to "A little learning goes a long way."
    }
}

@Composable
fun HomeScreen(
    onCategorySelected: (LearningCategory) -> Unit,
    onSettingsClick: () -> Unit = {}
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    var currentHour by remember {
        mutableIntStateOf(LocalTime.now().hour)
    }

    androidx.compose.runtime.DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                currentHour = LocalTime.now().hour
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val (greeting, subtitle) = greetingForHour(currentHour)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(homeBackground)
            .padding(horizontal = 14.dp)
            .padding(top = 12.dp)
    ) {
        // App branding and Settings entry point.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.lion_cub),
                contentDescription = "Kids Learn lion mascot",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.size(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Kids Learn",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = headingColor
                )

                Text(
                    text = "Little steps, big discoveries!",
                    fontSize = 12.sp,
                    color = subtitleColor
                )
            }

            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        Color.White,
                        RoundedCornerShape(16.dp)
                    )
            ) {
                Text(
                    text = "⚙",
                    fontSize = 24.sp,
                    color = headingColor
                )
            }
        }

        // Greeting based on the phone's local time.
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp),
            color = promptBackground
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 14.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = greeting,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = headingColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = subtitleColor,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Three-column category grid.
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = categories,
                key = { category -> category.name }
            ) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.88f)
                        .clickable {
                            onCategorySelected(category)
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
                            .padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = category.emoji,
                            fontSize = 38.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = category.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = headingColor,
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}
