package cc.atomtech.timetable.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RailwayAlert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.models.rfi.TrainData
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.components.animated.ShimmerLoading
import cc.atomtech.timetable.components.train.TrainCompactRow
import cc.atomtech.timetable.models.viewmodels.Station
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun Timetable(
    trainList: List<TrainData>?,
    stationData: Station,
    isDesktop: Boolean,
    openDetails: (TrainData) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(PaddingValues( end = 12.dp ))
    ) {
        if(trainList?.isEmpty() == true && !stationData.loadingDepartures.value && !stationData.loadingArrivals.value) {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Rounded.RailwayAlert,
                    contentDescription = StringRes.get("no_trains"),
                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(92.dp).height(92.dp)
                )
                Text(
                    StringRes.format("no_trains_details", Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding( horizontal = 32.dp, vertical = 12.dp ),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn {
                if (trainList?.isEmpty() == true && (stationData.loadingArrivals.value || stationData.loadingDepartures.value)) {
                    items( count = 5 ) {
                        ShimmerLoading(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp),
                            paddingValues = PaddingValues( vertical = 6.dp )
                        )
                    }
                }
                items(trainList ?: listOf()) { train ->
                    TrainCompactRow(train, openDetails = openDetails)
                    HorizontalDivider()
                }
                item {
                    Text(
                        StringRes.get("station_info"),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        stationData.info.value.trim()
                            .replace(Regex("\\s+"), " ")
                            .lowercase()
                            .replaceFirstChar {
                                if (it.isLowerCase())
                                    it.titlecase()
                                else
                                    it.toString()
                            }
                    )
                }
            }
        }
    }
}
