package cc.atomtech.timetable.components.animated

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ShimmerLoading(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {
    var shimmerState = remember { mutableStateOf(true) }
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 0.8f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val inverseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = 0.2f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val coroutineScope = rememberCoroutineScope()

    // Simulate loading delay
    LaunchedEffect(key1 = true) {
        delay(5) // Adjust the delay time as needed
        coroutineScope.launch {
            shimmerState.value = false
        }
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(paddingValues),
    ) {
        Box(
            modifier = modifier
        ) {
            val gradient = Brush.horizontalGradient(
                colors = listOf(
                    androidx.compose.material3.MaterialTheme.colorScheme.onSecondary.copy(alpha = alpha),
                    androidx.compose.material3.MaterialTheme.colorScheme.onSecondary.copy(alpha = inverseAlpha)
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradient)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (shimmerState.value) {
                    Spacer(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .background(androidx.compose.material3.MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                    )
                }
            }
        }
    }
}

