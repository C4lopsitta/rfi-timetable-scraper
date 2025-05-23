package cc.atomtech.timetable.views.management.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.AppPreferences
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.components.train.TrainCompactRow
import cc.atomtech.timetable.components.train.TrainDetailedRow
import cc.atomtech.timetable.enumerations.Category
import cc.atomtech.timetable.enumerations.Operator
import cc.atomtech.timetable.enumerations.TrainType
import cc.atomtech.timetable.enumerations.preferences.TrainRowDetailLevel
import cc.atomtech.timetable.models.rfi.TrainData
import cc.atomtech.timetable.models.rfi.TrainDelayStatus
import cc.atomtech.timetable.models.rfi.TrainStatus
import kotlinx.coroutines.flow.first

@Composable
fun TrainRowPreferences(
    appPreferences: AppPreferences
) {
    var trainRowDisplayMode by remember { mutableStateOf(TrainRowDetailLevel.COMPACT) }

    LaunchedEffect(Unit) {
        trainRowDisplayMode = appPreferences.getTrainRowDetailLevel().first()
    }

    LaunchedEffect(trainRowDisplayMode) {
        appPreferences.setTrainRowDetailLevel(trainRowDisplayMode)
    }

    val dummyTrainData = TrainData(
        operator = Operator.TRENITALIA,
        operatorName = "Trenitalia",
        category = Category.REG,
        categoryName = "Regionale",
        number = "12345",
        platform = "15",
        delay = TrainDelayStatus(15, TrainStatus.DELAYED),
        station = "Torino Porta Nuova",
        time = "10:15",
        stops = emptyList(),
        details = "",
        trainType = TrainType.DEPARTURE
    )

    LazyColumn (
        modifier = Modifier.padding( end = 12.dp )
    ) {
        item {
            Text(
                StringRes.get("settings_train_row_layout"),
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding( vertical = 12.dp )
            )
        }
        item {
            Column (
                modifier = Modifier.clickable(
                    onClick = {
                        trainRowDisplayMode = TrainRowDetailLevel.COMPACT
                    }
                )
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = trainRowDisplayMode == TrainRowDetailLevel.COMPACT,
                        onClick = {
                            trainRowDisplayMode = TrainRowDetailLevel.COMPACT
                        }
                    )
                    Text(
                        StringRes.get("settings_train_row_layout_compact"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(StringRes.get("settings_train_row_layout_compact_description"))
                TrainCompactRow( dummyTrainData, inDummyMode = true ) {

                }
            }
        }

        item {
            HorizontalDivider()
        }

        item {
            Column (
                modifier = Modifier.clickable(
                    onClick = {
                        trainRowDisplayMode = TrainRowDetailLevel.DETAILED
                    }
                )
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = trainRowDisplayMode == TrainRowDetailLevel.DETAILED,
                        onClick = {
                            trainRowDisplayMode = TrainRowDetailLevel.DETAILED
                        }
                    )
                    Text(
                        StringRes.get("settings_train_row_layout_detailed"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold )
                }
                Text(StringRes.get("settings_train_row_layout_detailed_description"))
                TrainDetailedRow( dummyTrainData, inDummyMode = true )
            }
        }
    }

}
