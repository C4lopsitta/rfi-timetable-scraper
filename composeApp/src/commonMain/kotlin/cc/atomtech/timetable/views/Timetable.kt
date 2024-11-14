package cc.atomtech.timetable.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.models.TrainData
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

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
            Text("No data")
        }
        LazyColumn() {
            items(trainList ?: listOf()) { train ->
                if(isDesktop) {
                    train.desktopRow(onTrainSelected)
                } else {
                    train.mobileRow(onTrainSelected)
                }
                HorizontalDivider()
            }
            if(stationInfo != null) {
                item {
                    Text("Station Information", fontSize = 20.sp, modifier = Modifier.padding( top = 16.dp ))
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
                        "Last update: ${Instant.ofEpochMilli(lastUpdate).atZone(ZoneId.systemDefault()).toLocalDateTime()}",
                        modifier = Modifier.padding( vertical = 12.dp )
                    )
                }
            }
        }
    }
}
