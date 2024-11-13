package cc.atomtech.rfi_timetable.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.rfi_timetable.models.TrainData
import java.util.Locale

@Composable
fun Timetable(trainList: List<TrainData>?,
              colorScheme: ColorScheme,
              stationInfo: String?,
              onTrainSelected: (TrainData) -> Unit) {
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
                train.mobileRow(onTrainSelected)
                Divider(
                    color = colorScheme.onSurface.copy(alpha = 0.1f),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if(stationInfo != null) {
                item {
                    HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )
                    Text("Station Information", fontSize = 20.sp)
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
                }
            }
        }
    }
}
