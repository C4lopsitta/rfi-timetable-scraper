package cc.atomtech.timetable.components.train

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.enumerations.CurrentStationType
import cc.atomtech.timetable.models.trenitalia.RestEasyTrainData

@Composable
fun TrainHeader(
    number: String,
    origin: String?,
    arrival: String
) {
    BaseHeader(
        number = number,
        origin = origin,
        arrival = arrival
    )
}

@Composable
fun TrainHeader(
    trainData: RestEasyTrainData
) {
    BaseHeader(
        number = "${trainData.trainNumber}",
        origin = trainData.origin,
        arrival = trainData.destination ?: "Undefined"
    )
}

@Composable
private fun BaseHeader(
    number: String,
    origin: String?,
    arrival: String
) {
    Text(StringRes.format("details_number", number))
    Text(
        arrival,
        fontSize = 32.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 38.sp
    )

    // "from" text if not line start
    if(origin != null) {
        Text(
            StringRes.get("from"),
            modifier = Modifier.padding( top = 4.dp ).fillMaxWidth(),
//                textAlign = TextAlign.End
        )
        Text(
            origin,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 38.sp,
//                textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}