package cc.atomtech.timetable.views.management.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import cc.atomtech.timetable.enumerations.ui.TrainRowDetailLevel
import cc.atomtech.timetable.models.rfi.TrainData
import cc.atomtech.timetable.models.rfi.TrainDelayStatus
import cc.atomtech.timetable.models.rfi.TrainStatus
import kotlinx.coroutines.flow.first

@Composable
fun TrainDataProviderPreferences(
    appPreferences: AppPreferences
) {
    LazyColumn (
        modifier = Modifier.padding( end = 12.dp )
    ) {
        item {
            Text("Choose what data provider you want to have priority when displaying data for a given train. If you choose \"Worst Scenario\", the provider giving the data with the highest delay will be used, if you choose \"Railway Operator\", the Railway Line operator will have priority (e.g. RFI in Italy), lastly, if you choose \"Train Operator\", the train operator (e.g. Trenitalia) will have priority. According to the data provider you choose, you might see vastly different results. You can always reset to \"View Mixed\" to get the worst scenario with the other results alongside it. If only one data source is available for any given train, it will be used regardless of the setting you select here.")
        }
    }
}
