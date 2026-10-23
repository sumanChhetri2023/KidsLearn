package com.education.kidslearn.ui.learning

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

fun lettersForSpelling(word: String): List<Char> =
    word.uppercase(Locale.US).filter { it in 'A'..'Z' }.toList()

@Composable
fun SpellingLetters(
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
