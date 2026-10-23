package com.education.kidslearn.ui.learning

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.education.kidslearn.data.LearningItem

@Composable
fun EducationalVisual(
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
