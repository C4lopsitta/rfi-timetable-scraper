package cc.atomtech.timetable.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.enumerations.CurrentStationType
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.models.rfi.TrainStopData
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.atDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DateTimeFormatBuilder
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayAt
import kotlinx.datetime.todayIn
import kotlin.time.Duration

@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
private fun TrainStopEntry(
    stop: TrainStopData,
    isLineStart: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    delay: Int
) {
    val lineColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
    val cancelledColor = androidx.compose.material3.MaterialTheme.colorScheme.error
    val circleCenterColor = androidx.compose.material3.MaterialTheme.colorScheme.background

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding( top = if(isLineStart) 12.dp else 0.dp )
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxHeight()
                .width(38.dp)
        ) {
            val yCenter = size.height / 2
            val baseXOffset = 24.dp

            if(isLineStart) {
                drawLine(
                    color = lineColor,
                    strokeWidth = 4.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect( intervals = floatArrayOf(2.dp.toPx(), 8.dp.toPx()), phase = 10.dp.toPx() ),
                    cap = StrokeCap.Round,
                    start = Offset( x = baseXOffset.toPx(), y = 0f ),
                    end = Offset( x = baseXOffset.toPx(), y = yCenter )
                )
            }

            drawLine(
                color = lineColor,
                strokeWidth = 4.dp.toPx(),
                start = Offset( x = baseXOffset.toPx(), y = if(isLineStart || isFirst) yCenter else 0f ),
                end = Offset( x = baseXOffset.toPx(), y = if(isLast) yCenter else size.height )
            )

            if(delay != Int.MIN_VALUE) {
                if (isLast || stop.isCurrentStop) {
                    drawCircle(
                        color = lineColor,
                        radius = 12.dp.toPx(),
                        center = Offset(x = baseXOffset.toPx(), y = yCenter)
                    )
                    drawCircle(
                        color = circleCenterColor,
                        radius = 8.dp.toPx(),
                        center = Offset(x = baseXOffset.toPx(), y = yCenter)
                    )
                } else {
                    drawLine(
                        color = lineColor,
                        strokeWidth = 4.dp.toPx(),
                        start = Offset(x = (baseXOffset + 12.dp).toPx(), y = yCenter),
                        end = Offset(x = baseXOffset.toPx(), y = yCenter)
                    )
                }
            } else {
                drawLine(
                    color = cancelledColor,
                    strokeWidth = 4.dp.toPx(),
                    start = Offset(x = (baseXOffset + 12.dp).toPx(), y = (yCenter + 12.dp.toPx())),
                    end = Offset(x = (baseXOffset - 12.dp).toPx(), y = (yCenter - 12.dp.toPx()))
                )
                drawLine(
                    color = cancelledColor,
                    strokeWidth = 4.dp.toPx(),
                    start = Offset(x = (baseXOffset - 12.dp).toPx(), y = (yCenter + 12.dp.toPx())),
                    end = Offset(x = (baseXOffset + 12.dp).toPx(), y = (yCenter - 12.dp.toPx()))
                )
            }
        }
        Column (
            modifier = Modifier
                .padding( start = 48.dp )
                .height(80.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stop.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                if(delay == Int.MIN_VALUE) {
                    StringRes.get("cancelled")
                } else if(delay == Int.MAX_VALUE) {
                    stop.time + " - " + StringRes.get("delayed")
                } else if(delay != 0) {
                    var delayHM = ""
                    if(delay < 60) {
                        delayHM = "${delay}m"
                    } else {
                        val hours = delay % 60
                        delayHM = "${hours}h ${delay - hours * 60}m"
                    }

                    val newTime = LocalTime
                        .parse(stop.time, format = LocalTime.Format { byUnicodePattern("H:mm") })
                        .atDate(Clock.System.todayIn(TimeZone.currentSystemDefault()))
                        .toInstant(TimeZone.currentSystemDefault())
                        .plus(Duration.parse(delayHM))
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                    StringRes.format("detail_stop_delayed_string", "${newTime.hour}:${newTime.minute}", stop.time)
                } else {
                    stop.time
                }
            )
        }
    }
}


@Composable
fun TrainStopList(
    stationType: CurrentStationType,
    stops: List<TrainStopData>,
    delay: Int
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            stops.forEach { stop ->
                TrainStopEntry(
                    stop = stop,
                    delay = delay,
                    isLineStart = stationType == CurrentStationType.STOP && stops.indexOf(stop) == 0,
                    isFirst = stops.indexOf(stop) == 0,
                    isLast = stops.indexOf(stop) == stops.size -1
                )
            }
        }
    }
}
