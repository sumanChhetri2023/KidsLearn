package com.education.kidslearn.ui.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val settingsBackground = Color(0xFFFFFBF5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    musicEnabled: Boolean,
    onMusicChanged: (Boolean) -> Unit,
    musicVolumeLevel: Int,
    onMusicVolumeChanged: (Int) -> Unit,
    effectsEnabled: Boolean,
    onEffectsChanged: (Boolean) -> Unit,
    spokenWordsEnabled: Boolean,
    onSpokenWordsChanged: (Boolean) -> Unit,
    quizQuestionCount: Int,
    onQuizQuestionCountChanged: (Int) -> Unit,
    availableQuizCategories: List<String>,
    selectedQuizCategories: Set<String>,
    onQuizCategoriesChanged: (Set<String>) -> Unit
) {
    BackHandler(onBack = onBackClick)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    Text(
                        text = "←",
                        fontSize = 28.sp,
                        modifier = Modifier
                            .clickable(onClick = onBackClick)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = settingsBackground
                )
            )
        },
        containerColor = settingsBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Background music
            SettingsSwitchRow(
                icon = "🎵",
                title = "Background music",
                subtitle = "Play gentle music while the app is active",
                checked = musicEnabled,
                onCheckedChange = onMusicChanged
            )

            MusicVolumeSelector(
                enabled = musicEnabled,
                selectedLevel = musicVolumeLevel,
                onLevelSelected = onMusicVolumeChanged
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Sound effects
            SettingsSwitchRow(
                icon = "🔊",
                title = "Sound effects",
                subtitle = "Enable short interaction sounds",
                checked = effectsEnabled,
                onCheckedChange = onEffectsChanged
            )

            // Spoken words
            SettingsSwitchRow(
                icon = "🎤",
                title = "Spoken words",
                subtitle = "Enable item announcements and spelling",
                checked = spokenWordsEnabled,
                onCheckedChange = onSpokenWordsChanged
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Quiz settings
            Text(
                text = "Quiz Settings",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF343452)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Questions per quiz",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Choose how many questions your child will answer.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF616161)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(5, 10, 15, 20).forEach { count ->
                            FilterChip(
                                selected = quizQuestionCount == count,
                                onClick = {
                                    onQuizQuestionCountChanged(count)
                                },
                                label = {
                                    Text("$count")
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quiz category selection
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Quiz Categories",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Choose which categories can appear in quizzes.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF616161)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val allSelected =
                        availableQuizCategories.isNotEmpty() &&
                                availableQuizCategories.all {
                                    it in selectedQuizCategories
                                }

                    // Select all categories
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (allSelected) {
                                    // Keep at least one category selected.
                                    availableQuizCategories.firstOrNull()?.let {
                                        onQuizCategoriesChanged(setOf(it))
                                    }
                                } else {
                                    onQuizCategoriesChanged(
                                        availableQuizCategories.toSet()
                                    )
                                }
                            }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = allSelected,
                            onCheckedChange = { checked ->
                                if (checked) {
                                    onQuizCategoriesChanged(
                                        availableQuizCategories.toSet()
                                    )
                                } else {
                                    // Keep at least one category selected.
                                    availableQuizCategories.firstOrNull()?.let {
                                        onQuizCategoriesChanged(setOf(it))
                                    }
                                }
                            }
                        )

                        Text(
                            text = "Select all categories",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    HorizontalDivider(
                        color = Color(0xFFEEEEEE)
                    )

                    // Individual categories
                    availableQuizCategories.forEach { category ->
                        val isSelected =
                            category in selectedQuizCategories

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val updated = selectedQuizCategories.toMutableSet()

                                    if (isSelected) {
                                        if (selectedQuizCategories.size > 1) {
                                            updated.remove(category)
                                            onQuizCategoriesChanged(updated)
                                        }
                                    } else {
                                        updated.add(category)
                                        onQuizCategoriesChanged(updated)
                                    }
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { checked ->
                                    val updated =
                                        selectedQuizCategories.toMutableSet()

                                    if (checked) {
                                        updated.add(category)
                                        onQuizCategoriesChanged(updated)
                                    } else if (selectedQuizCategories.size > 1) {
                                        updated.remove(category)
                                        onQuizCategoriesChanged(updated)
                                    }
                                }
                            )

                            Text(
                                text = category,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    if (availableQuizCategories.isEmpty()) {
                        Text(
                            text = "No quiz categories are available.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF616161)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // About Kids Learn
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "About Kids Learn",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "A fun way for children to learn through pictures, " +
                                "pronunciation and quizzes.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF616161)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MusicVolumeSelector(
    enabled: Boolean,
    selectedLevel: Int,
    onLevelSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Music volume",
                    style = MaterialTheme.typography.titleSmall,
                    color = if (enabled) {
                        Color(0xFF343452)
                    } else {
                        Color.Gray
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = when (selectedLevel.coerceIn(0, 2)) {
                        0 -> "Low"
                        1 -> "Mid"
                        else -> "High"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    color = if (enabled) {
                        Color(0xFF5C58A5)
                    } else {
                        Color.Gray
                    }
                )
            }

            Slider(
                value = selectedLevel.coerceIn(0, 2).toFloat(),
                onValueChange = { value ->
                    onLevelSelected(value.toInt().coerceIn(0, 2))
                },
                valueRange = 0f..2f,
                steps = 1,
                enabled = enabled
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Low",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "Mid",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "High",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    icon: String,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 26.sp,
                modifier = Modifier.padding(end = 14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF616161)
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}
