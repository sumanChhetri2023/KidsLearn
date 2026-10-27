package com.education.kidslearn.ui.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val settingsBackground = Color(0xFFFFFBF5)
private val sectionBorder = Color(0xFFD3CCC2)
private val sectionShape = RoundedCornerShape(12.dp)

private const val PRIVACY_POLICY_URL =
    "https://sites.google.com/view/kidslearn-privacy-policy/home"

private const val DEVELOPER_EMAIL = "nyralabs@gmail.com"

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

    val context = LocalContext.current
    var showAppInfo by remember { mutableStateOf(false) }

    val appVersion = remember {
        try {
            context.packageManager
                .getPackageInfo(context.packageName, 0)
                .versionName ?: "Unknown"
        } catch (_: Exception) {
            "Unknown"
        }
    }

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
                            .padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            )
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
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // 1. Background music and volume share one border.
            SettingsSection {
                Column {
                    SettingsSwitchRow(
                        icon = "🎵",
                        title = "Background music",
                        subtitle = "Play gentle music while the app is active",
                        checked = musicEnabled,
                        onCheckedChange = onMusicChanged
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        ),
                        color = Color(0xFFEAE5DE),
                        thickness = 1.dp
                    )

                    MusicVolumeSelector(
                        enabled = musicEnabled,
                        selectedLevel = musicVolumeLevel,
                        onLevelSelected = onMusicVolumeChanged
                    )
                }
            }

            // 2. Sound effects.
            SettingsSection {
                SettingsSwitchRow(
                    icon = "🔊",
                    title = "Sound effects",
                    subtitle = "Enable short interaction sounds",
                    checked = effectsEnabled,
                    onCheckedChange = onEffectsChanged
                )
            }

            // 3. Spoken words.
            SettingsSection {
                SettingsSwitchRow(
                    icon = "🎤",
                    title = "Spoken words",
                    subtitle = "Enable item announcements and spelling",
                    checked = spokenWordsEnabled,
                    onCheckedChange = onSpokenWordsChanged
                )
            }

            // 4. Quiz questions and categories share one border.
            SettingsSection {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 10.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Quiz Settings",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF343452)
                    )

                    Text(
                        text = "Questions per quiz",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(5, 10, 15, 20).forEach { count ->
                            FilterChip(
                                selected = quizQuestionCount == count,
                                onClick = {
                                    onQuizQuestionCountChanged(count)
                                },
                                label = {
                                    Text(
                                        text = "$count",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    HorizontalDivider(
                        color = Color(0xFFEAE5DE),
                        thickness = 1.dp
                    )

                    val allSelected =
                        availableQuizCategories.isNotEmpty() &&
                                availableQuizCategories.all {
                                    it in selectedQuizCategories
                                }

                    // Select all categories.
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (availableQuizCategories.isNotEmpty()) {
                                    if (allSelected) {
                                        availableQuizCategories.firstOrNull()
                                            ?.let {
                                                onQuizCategoriesChanged(setOf(it))
                                            }
                                    } else {
                                        onQuizCategoriesChanged(
                                            availableQuizCategories.toSet()
                                        )
                                    }
                                }
                            }
                            .padding(vertical = 2.dp),
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
                                    availableQuizCategories.firstOrNull()
                                        ?.let {
                                            onQuizCategoriesChanged(setOf(it))
                                        }
                                }
                            },
                            modifier = Modifier.size(36.dp)
                        )

                        Text(
                            text = "Select all categories",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // Dense two-column category list.
                    if (availableQuizCategories.isNotEmpty()) {
                        availableQuizCategories.chunked(2).forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                rowItems.forEach { category ->
                                    val isSelected =
                                        category in selectedQuizCategories

                                    Row(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                val updated =
                                                    selectedQuizCategories
                                                        .toMutableSet()

                                                if (isSelected) {
                                                    if (updated.size > 1) {
                                                        updated.remove(category)
                                                        onQuizCategoriesChanged(
                                                            updated
                                                        )
                                                    }
                                                } else {
                                                    updated.add(category)
                                                    onQuizCategoriesChanged(
                                                        updated
                                                    )
                                                }
                                            },
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = isSelected,
                                            onCheckedChange = { checked ->
                                                val updated =
                                                    selectedQuizCategories
                                                        .toMutableSet()

                                                if (checked) {
                                                    updated.add(category)
                                                    onQuizCategoriesChanged(
                                                        updated
                                                    )
                                                } else if (updated.size > 1) {
                                                    updated.remove(category)
                                                    onQuizCategoriesChanged(
                                                        updated
                                                    )
                                                }
                                            },
                                            modifier = Modifier.size(36.dp)
                                        )

                                        Text(
                                            text = category,
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 2
                                        )
                                    }
                                }

                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "No quiz categories are available.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF616161)
                        )
                    }
                }
            }

            // 5. About, privacy, contact and app information.
            SettingsSection {
                Column(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "About Kids Learn",
                        modifier = Modifier.padding(
                            start = 12.dp,
                            top = 8.dp,
                            bottom = 2.dp
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFF343452)
                    )

                    Text(
                        text = "A fun way for children to learn through pictures, pronunciation and quizzes.",
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 2.dp
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF616161)
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 4.dp
                        ),
                        color = Color(0xFFEAE5DE),
                        thickness = 1.dp
                    )

                    AboutActionRow(
                        icon = "🛡️",
                        title = "Privacy Policy",
                        subtitle = "Read our privacy policy",
                        onClick = {
                            openExternalUrl(
                                context,
                                PRIVACY_POLICY_URL
                            )
                        }
                    )

                    AboutActionRow(
                        icon = "✉️",
                        title = "Contact & Feedback",
                        subtitle = "Send suggestions or report a problem",
                        onClick = {
                            val emailIntent = Intent(
                                Intent.ACTION_SENDTO,
                                Uri.parse("mailto:$DEVELOPER_EMAIL")
                            ).apply {
                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "Kids Learn - Feedback"
                                )
                            }

                            try {
                                context.startActivity(emailIntent)
                            } catch (_: ActivityNotFoundException) {
                                Toast.makeText(
                                    context,
                                    "No email app is available on this device.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    )

                    AboutActionRow(
                        icon = "⭐",
                        title = "Rate the App",
                        subtitle = "Share your feedback on Google Play",
                        onClick = {
                            val packageName = context.packageName

                            try {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(
                                            "market://details?id=$packageName"
                                        )
                                    )
                                )
                            } catch (_: ActivityNotFoundException) {
                                openExternalUrl(
                                    context,
                                    "https://play.google.com/store/apps/details?id=$packageName"
                                )
                            }
                        }
                    )

                    AboutActionRow(
                        icon = "ℹ️",
                        title = "App Information",
                        subtitle = "Version $appVersion and developer details",
                        onClick = {
                            showAppInfo = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    // App Information dialog.
    if (showAppInfo) {
        AlertDialog(
            onDismissRequest = {
                showAppInfo = false
            },
            title = {
                Text("App Information")
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Kids Learn",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text("Version: $appVersion")

                    Text(
                        text = "An educational app designed to help children learn through pictures, pronunciation and quizzes."
                    )

                    Text("Developer contact: $DEVELOPER_EMAIL")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAppInfo = false
                    }
                ) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun SettingsSection(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = sectionBorder,
                shape = sectionShape
            ),
        content = content
    )
}

@Composable
private fun MusicVolumeSelector(
    enabled: Boolean,
    selectedLevel: Int,
    onLevelSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.padding(
            horizontal = 12.dp,
            vertical = 6.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Music volume",
                style = MaterialTheme.typography.titleSmall,
                color = if (enabled) Color(0xFF343452) else Color.Gray
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = when (selectedLevel.coerceIn(0, 2)) {
                    0 -> "Low"
                    1 -> "Mid"
                    else -> "High"
                },
                style = MaterialTheme.typography.labelLarge,
                color = if (enabled) Color(0xFF5C58A5) else Color.Gray
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
            Text("Low", style = MaterialTheme.typography.bodySmall)
            Text("Mid", style = MaterialTheme.typography.bodySmall)
            Text("High", style = MaterialTheme.typography.bodySmall)
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCheckedChange(!checked)
            }
            .padding(
                horizontal = 12.dp,
                vertical = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 24.sp,
            modifier = Modifier.padding(end = 10.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )

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

@Composable
private fun AboutActionRow(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = 12.dp,
                vertical = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 22.sp,
            modifier = Modifier.padding(end = 10.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF343452)
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF616161)
            )
        }

        Text(
            text = "›",
            fontSize = 24.sp,
            color = Color(0xFF777777)
        )
    }
}

private fun openExternalUrl(
    context: Context,
    url: String
) {
    try {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
        )
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "No browser is available on this device.",
            Toast.LENGTH_LONG
        ).show()
    }
}
