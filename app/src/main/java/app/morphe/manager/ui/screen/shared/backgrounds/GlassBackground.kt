/*
 * Copyright 2026 Glass Patcher contributors.
 *
 * Glassmorphic aurora backdrop for Glass Patcher, inspired by the glassmorphic UI kit.
 */

package app.morphe.manager.ui.screen.shared.backgrounds

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlin.math.PI
import kotlin.math.sin

/**
 * Soft aurora blobs that drift slowly behind translucent glass surfaces.
 *
 * The blobs are drawn as large radial gradients rather than hard circles, which gives the
 * frosted, blurred look of the glassmorphic UI kit without needing a render effect.
 */
@Composable
fun GlassBackground(
    modifier: Modifier = Modifier,
    enableParallax: Boolean = true,
    speedMultiplier: Float = 1f,
    patchingCompleted: Boolean = false
) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val tertiary = MaterialTheme.colorScheme.tertiary
    val context = LocalContext.current

    val parallaxState = rememberParallaxState(
        enableParallax = enableParallax,
        sensitivity = 0.25f,
        context = context
    )

    val time = rememberAnimatedTime(speedMultiplier)

    Canvas(modifier = modifier.fillMaxSize()) {
        val t = time.value
        val tiltX = parallaxState.tiltX.value
        val tiltY = parallaxState.tiltY.value
        val twoPi = 2f * PI.toFloat()
        val width = size.width
        val height = size.height

        // A deep navy base keeps the translucent surfaces readable.
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0B0E1C),
                    Color(0xFF070A14),
                    Color(0xFF05060D)
                )
            )
        )

        val blobs = listOf(
            Blob(
                x = 0.16f + 0.05f * sin(t * twoPi / 11000f),
                y = 0.12f + 0.04f * sin(t * twoPi / 9000f),
                radius = 0.72f,
                color = primary,
                alpha = 0.38f,
                depth = 0.8f
            ),
            Blob(
                x = 0.86f + 0.04f * sin(t * twoPi / 13000f),
                y = 0.24f + 0.05f * sin(t * twoPi / 10000f),
                radius = 0.62f,
                color = secondary,
                alpha = 0.30f,
                depth = 0.6f
            ),
            Blob(
                x = 0.58f + 0.05f * sin(t * twoPi / 12000f),
                y = 0.84f + 0.04f * sin(t * twoPi / 9500f),
                radius = 0.68f,
                color = tertiary,
                alpha = 0.26f,
                depth = 0.7f
            )
        )

        blobs.forEach { blob ->
            val center = Offset(
                x = width * blob.x + tiltX * blob.depth * 36f,
                y = height * blob.y + tiltY * blob.depth * 36f
            )
            val radius = width.coerceAtLeast(height) * blob.radius
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        blob.color.copy(alpha = blob.alpha),
                        blob.color.copy(alpha = blob.alpha * 0.45f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = radius
                ),
                radius = radius,
                center = center
            )
        }

        // A faint top sheen makes the glass surfaces feel lit from above.
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.White.copy(alpha = 0.035f), Color.Transparent),
                startY = 0f,
                endY = height * 0.45f
            )
        )
    }
}

private data class Blob(
    val x: Float,
    val y: Float,
    val radius: Float,
    val color: Color,
    val alpha: Float,
    val depth: Float
)
