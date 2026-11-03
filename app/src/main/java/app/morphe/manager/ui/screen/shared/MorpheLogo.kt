/*
 * Copyright 2026 Morphe.
 * https://github.com/MorpheApp/morphe-manager
 */

package app.morphe.manager.ui.screen.shared

import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.createBitmap
import app.morphe.manager.R

/**
 * The Morphe launcher logo in its own colors, for the white circle Morphe itself is shown in: the
 * source it ships with, and its own update. Scaled up past the foreground's safe zone to fill it.
 */
@Composable
fun MorpheLauncherLogo(modifier: Modifier = Modifier) {
    Image(
        painter = rememberAppCompatPainter(R.drawable.ic_launcher_foreground),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier.graphicsLayer {
            scaleX = 1.5f
            scaleY = 1.5f
        }
    )
}

/**
 * The Glass Patcher wordmark: a small gradient mark followed by the product name in the theme's
 * surface color, so it reads on whatever the animated background lays under it.
 */
@Composable
fun MorpheWordmark(
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "▶",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "GLASS",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 19.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.4).sp
            )
        )
        Text(
            text = " PATCHER",
            color = textColor,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 19.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = (-0.2).sp
            )
        )
    }
}

/**
 * Painter for a vector of the Morphe branding, loaded through AppCompat as `painterResource` cannot
 * be trusted with them in release builds, see [rememberMorpheLogoBitmap].
 */
@Composable
private fun rememberAppCompatPainter(@DrawableRes id: Int): Painter {
    val context = LocalContext.current
    return rememberDrawablePainter(drawable = remember(context, id) { AppCompatResources.getDrawable(context, id) })
}

/**
 * Monochrome Morphe logo rasterized for use with a tinted [androidx.compose.material3.Icon].
 * Loaded through AppCompat and drawn to a bitmap because `painterResource` crashes on this vector in release builds.
 */
@Composable
fun rememberMorpheLogoBitmap(): ImageBitmap? {
    val context = LocalContext.current
    return remember(context) {
        runCatching {
            val drawable = AppCompatResources.getDrawable(context, R.drawable.ic_mpp) ?: return@runCatching null
            val size = 96
            // The path spans nearly the full viewport, so inset it to Material's 20dp live area
            val inset = size / 24
            val bmp = createBitmap(size, size)
            drawable.setBounds(inset, inset, size - inset, size - inset)
            drawable.draw(Canvas(bmp))
            bmp.asImageBitmap()
        }.getOrNull()
    }
}
