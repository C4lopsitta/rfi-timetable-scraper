package cc.atomtech.timetable.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.models.TrainData
import cc.atomtech.timetable.StringRes

@Composable
fun TrainDetails(trainData: TrainData?,
                 isArrival: Boolean) {
    Column (
        modifier = Modifier.fillMaxSize().padding( end = 12.dp ).verticalScroll(rememberScrollState())
    ) {
        Text(StringRes.format("details_number", "${trainData?.number}"))
        Text(trainData?.station ?: StringRes.get("undefined"), fontSize = 32.sp, fontWeight = FontWeight.SemiBold, lineHeight = 38.sp)
        HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(if (isArrival) StringRes.get("details_arrives") else StringRes.get("details_departs"))
                Text(trainData?.time ?: "--:--", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            }
            Column (
                horizontalAlignment = Alignment.End
            ) {
                Text( StringRes.get("details_from_platform"))
                Text( trainData?.platform ?: StringRes.get("platform_to_be_announced"),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold )
            }
        }
        if(trainData?.getDelayString(addSpace = false) != null) {
            Text(StringRes.get("delay"), modifier = Modifier.padding( top = 12.dp ) )
            Text(trainData.getDelayString(addSpace = false) ?: "",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold)
        }
        HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(StringRes.get("operator"))
                Text(trainData?.operator.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold)
            }
            Column (
                horizontalAlignment = Alignment.End
            ) {
                Text( StringRes.get("service") )
                Text( trainData?.category.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold )
            }
        }
        if(trainData?.details != null) {
            if(trainData.details.isNotEmpty()) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Text(StringRes.get("details"))
                Text(trainData.details ?: "")
            }
        }
        if(trainData?.stops != null) {
            if(trainData.stops.isNotEmpty()) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Text(if (isArrival) StringRes.get("previous_stops") else StringRes.get("next_stops"))
                trainData.stops.forEach { stop -> stop.build() }
            }
        }
    }
}
