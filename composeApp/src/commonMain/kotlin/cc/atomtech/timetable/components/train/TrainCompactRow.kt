package cc.atomtech.timetable.components.train

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.enumerations.Category
import cc.atomtech.timetable.extenions.toStationName
import cc.atomtech.timetable.models.rfi.TrainData


// TODO)) fix various issues
@Composable
fun TrainCompactRow(
    trainData: TrainData,
    inDummyMode: Boolean = false,
    openDetails: (TrainData) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(vertical = 10.dp))
            .clickable(interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                role  = Role.Button,
                onClickLabel = StringRes.get("click_for_details"),
                onClick = {
                    if(inDummyMode) return@clickable
                    openDetails(trainData)
                }
            )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface (
                modifier = Modifier.padding(PaddingValues.Absolute(right = 24.dp)),
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(60.dp)
                ) {
                    val trainCategory = trainData.category.toShortString()
                    if(trainData.category != Category.UNDEFINED) {
                        Text(trainCategory,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Rounded.Train, contentDescription = trainData.operator.toString())
                    }
                    Text("${trainData.number}")
                }
            }

            Column (
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    trainData.station?.toStationName() ?: StringRes.get("undefined"),
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold )
                var timeString = "${trainData.time}"
                timeString += trainData.delay.toString( padded = true )
                if(timeString.contains(StringRes.get("cancelled"))) timeString = StringRes.get("cancelled")
                Text(timeString)
            }

            Column (
                modifier = Modifier.width(80.dp)
            ) {
                val platform = if(trainData.platform.isNullOrEmpty()) null else trainData.platform
                Text(
                    StringRes.get("platform"),
                    fontSize = 12.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth())
                Text(trainData.platform ?: "-",
                    fontSize = 16.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth())
            }
        }
    }
}