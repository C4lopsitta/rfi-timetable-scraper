package cc.atomtech.timetable.components.animated

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize

@Composable
fun ShimmerBox(modifier: Modifier) {
    val transition = rememberInfiniteTransition()
    var size by remember { mutableStateOf(Size.Zero) }
    val shimmerAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier
            .onGloballyPositioned { size = it.size.toSize() }
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        androidx.compose.material3.MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                        androidx.compose.material3.MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.2f),
                        androidx.compose.material3.MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(
                        x = size.width * shimmerAnimation.value * 2,
                        y = 0f
                    ),
                    tileMode = TileMode.Decal
                )
            )
    )
}
