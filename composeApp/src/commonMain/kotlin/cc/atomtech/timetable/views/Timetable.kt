package cc.atomtech.timetable.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RailwayAlert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.models.TrainData
import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import cc.atomtech.timetable.StringRes

@Composable
fun Timetable(trainList: List<TrainData>?,
              stationInfo: String?,
              onTrainSelected: (TrainData) -> Unit,
              lastUpdate: Long,
              isDesktop: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(PaddingValues(0.dp, 12.dp, 12.dp, 12.dp))
    ) {
        if(trainList?.isEmpty() == true) {
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
                    StringRes.format("no_trains_details", Instant.ofEpochMilli(lastUpdate).atZone(ZoneId.systemDefault()).toLocalDateTime()),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding( horizontal = 32.dp, vertical = 12.dp ),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn {
                items(trainList ?: listOf()) { train ->
                    if (isDesktop) {
                        train.desktopRow(onTrainSelected)
                    } else {
                        train.mobileRow(onTrainSelected)
                    }
                    HorizontalDivider()
                }
                if (stationInfo != null) {
                    item {
                        Text(
                            StringRes.get("station_info"),
                            fontSize = 20.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            stationInfo
                                .trim()
                                .replace(Regex("\\s+"), " ")
                                .lowercase(Locale.getDefault())
                                .replaceFirstChar {
                                    if (it.isLowerCase())
                                        it.titlecase(Locale.getDefault())
                                    else
                                        it.toString()
                                }
                        )
                        Text(
                            "${StringRes.get("last_update")}: ${
                                Instant.ofEpochMilli(lastUpdate).atZone(ZoneId.systemDefault())
                                    .toLocalDateTime()
                            }",
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
            }
        }
    }
}
