package com.education.kidslearn.ui.learning

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
fun ItemPicture(
    item: LearningItem,
    bitmapCache: MutableMap<String, ImageBitmap>,
    modifier: Modifier = Modifier,
    animationScale: Float = 1f
) {
    val context = LocalContext.current

    val flipHorizontally = remember(item.name) {
        kotlin.random.Random.nextBoolean()
    }

    val isEducationalVisual =
        item.name.all { it.isDigit() } ||
                item.name in setOf(
            "Circle", "Square", "Triangle", "Rectangle",
            "Star", "Heart", "Diamond", "Oval"
        )

    val bitmap = produceState<ImageBitmap?>(
        initialValue = if (isEducationalVisual) {
            null
        } else {
            item.assetPath?.let { bitmapCache[it] }
        },
        item.assetPath,
        item.name,
        bitmapCache,
        context
    ) {
        val path = item.assetPath

        if (isEducationalVisual || path == null) {
            value = null
        } else {
            val cachedBitmap = bitmapCache[path]

            if (cachedBitmap != null) {
                value = cachedBitmap
            } else {
                val decodedBitmap = withContext(Dispatchers.IO) {
                    loadItemBitmap(context, path)
                }

                decodedBitmap?.let {
                    bitmapCache[path] = it
                }

                value = decodedBitmap
            }
        }
    }

    if (isEducationalVisual) {
        EducationalVisual(
            item = item,
            modifier = modifier.scale(animationScale)
        )
    } else if (bitmap.value != null) {
        Image(
            bitmap = bitmap.value!!,
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

private fun loadItemBitmap(
    context: Context,
    path: String
): ImageBitmap? {

    // Preferred location: app/src/main/assets/<category>/<file>.png
    val fromAssets = runCatching {
        context.assets.open(path).use { stream ->
            BitmapFactory.decodeStream(stream)?.asImageBitmap()
        }
    }.onFailure { error ->
        Log.d(
            "KidsLearn",
            "Asset lookup failed for $path; trying drawable resources.",
            error
        )
    }.getOrNull()

    if (fromAssets != null) {
        return fromAssets
    }

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

    if (resourceId == 0) {
        Log.w(
            "KidsLearn",
            "Image not found for path: $path"
        )
        return null
    }

    return runCatching {
        BitmapFactory.decodeResource(
            context.resources,
            resourceId
        )?.asImageBitmap()
    }.onFailure { error ->
        Log.w(
            "KidsLearn",
            "Unable to decode drawable resource: $resourceName",
            error
        )
    }.getOrNull()
}
