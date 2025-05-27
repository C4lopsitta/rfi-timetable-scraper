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
import cc.atomtech.timetable.enumerations.ui.TrainHeaderDisplayOrder
import cc.atomtech.timetable.models.trenitalia.RestEasyTrainData

@Composable
fun TrainHeader(
    number: String,
    origin: String?,
    arrival: String,
    displayOrder: TrainHeaderDisplayOrder = TrainHeaderDisplayOrder.ARRIVAL_FIRST
) {
    BaseHeader(
        number = number,
        origin = origin,
        arrival = arrival,
        displayOrder = displayOrder
    )
}

@Composable
fun TrainHeader(
    trainData: RestEasyTrainData,
    displayOrder: TrainHeaderDisplayOrder = TrainHeaderDisplayOrder.ARRIVAL_FIRST
) {
    BaseHeader(
        number = "${trainData.trainNumber}",
        origin = trainData.origin,
        arrival = trainData.destination ?: "Undefined",
        displayOrder = displayOrder
    )
}

@Composable
private fun BaseHeader(
    number: String,
    origin: String?,
    arrival: String,
    displayOrder: TrainHeaderDisplayOrder = TrainHeaderDisplayOrder.ARRIVAL_FIRST
) {
    if(displayOrder == TrainHeaderDisplayOrder.ORIGIN_FIRST && origin == null)
        throw Exception("Origin cannot be null if order is ORIGIN_FIRST")

    Text(StringRes.format("train_header_number_${if(displayOrder == TrainHeaderDisplayOrder.ARRIVAL_FIRST) "to" else "from"}", number))
    Text(
        if(displayOrder == TrainHeaderDisplayOrder.ARRIVAL_FIRST) arrival else origin!!,
        fontSize = 32.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 38.sp
    )

    // "from" text if not line start
    if(origin != null) {
        Text(
            StringRes.get(
                if(displayOrder == TrainHeaderDisplayOrder.ARRIVAL_FIRST) "from" else "to"
            ),
            modifier = Modifier.padding( top = 4.dp ).fillMaxWidth(),
//                textAlign = TextAlign.End
        )
        Text(
            if(displayOrder == TrainHeaderDisplayOrder.ARRIVAL_FIRST) origin else arrival,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 38.sp,
//                textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}