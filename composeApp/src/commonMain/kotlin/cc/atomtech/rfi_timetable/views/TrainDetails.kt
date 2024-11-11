package cc.atomtech.rfi_timetable.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.rfi_timetable.models.TrainData

@Composable
fun TrainDetails(trainData: TrainData?,
                 isArrival: Boolean) {

    Column (
        modifier = Modifier.fillMaxSize().padding( end = 12.dp ),
    ) {
        Text("Train number ${trainData?.number} to")
        Text(trainData?.station ?: "No data", fontSize = 32.sp, fontWeight = FontWeight.SemiBold, lineHeight = 38.sp)
        HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text("${if (isArrival) "Arrives" else "Departs"} at")
                Text(trainData?.time ?: "--:--", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            }
            Column (
                horizontalAlignment = Alignment.End
            ) {
                Text( "From platform" )
                Text( trainData?.platform ?: "--",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold )
            }
        }
        if(trainData?.getDelayString(addSpace = false) != null) {
            Text("Delay", modifier = Modifier.padding( top = 12.dp ) )
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
                Text("Operator")
                Text(trainData?.operator.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold)
            }
            Column (
                horizontalAlignment = Alignment.End
            ) {
                Text( "Service" )
                Text( trainData?.category.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold )
            }
        }
        HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )
        Text("Details")
        HorizontalDivider( modifier = Modifier.padding( vertical = 12.dp ) )
        Text(if(isArrival) "Stopped in" else "Stops in")

    }
}
