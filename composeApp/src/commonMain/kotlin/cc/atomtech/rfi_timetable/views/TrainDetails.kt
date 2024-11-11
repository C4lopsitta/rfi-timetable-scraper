package cc.atomtech.rfi_timetable.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cc.atomtech.rfi_timetable.models.TrainData

@Composable
fun TrainDetails(trainData: TrainData?) {

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        Text("more details\n${trainData?.number ?: "AAAA"}")
    }
}
