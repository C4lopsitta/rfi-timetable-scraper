package cc.atomtech.timetable.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cc.atomtech.timetable.models.TrenitaliaInfoLavori

@Composable
fun TrenitaliaRegionInfo(selectedRegion: TrenitaliaInfoLavori?) {
    Column {
        Text(selectedRegion?.regionName ?: "AAAAA")
    }
}