package com.education.kidslearn.ui.learning

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.education.kidslearn.data.LearningItem
import java.util.Locale

@Composable
fun ItemPicture(
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
