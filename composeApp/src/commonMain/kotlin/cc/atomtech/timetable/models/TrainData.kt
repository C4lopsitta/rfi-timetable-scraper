package cc.atomtech.timetable.models

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.enumerations.Category
import cc.atomtech.timetable.enumerations.Operator
import cc.atomtech.timetable.Strings

data class Stop(val stationName: String, val time: String?) {
    @Composable
    fun build() {
        Column (
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
        ) {
            Text(stationName, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text(time ?: "")
        }
    }
}

class TrainData(
    val operator: Operator,
    private val operatorName: String?,
    val number: String?,
    val category: Category,
    var platform: String?,
    val delay: Int,
    val station: String? = null,
    val time: String? = null,
    val stops: List<Stop>,
    val details: String? = null) {

    fun getDelayString(addSpace: Boolean = true): String? {
        if (time != null) {
            when (delay) {
                Int.MAX_VALUE -> {
                    return "${if(addSpace) " " else ""}${Strings.get("delayed")}"
                }
                Int.MIN_VALUE -> {
                    return Strings.get("cancelled")
                }
                0 -> { return if (addSpace) "" else null }
                else -> {
                    return "${if(addSpace) " " else ""}+$delay ${Strings.get("minutes")}"
                }
            }
        }
        return null
    }

    @Composable
    fun desktopRow(viewDetails: (TrainData) -> Unit) {
        return this.mobileRow { viewDetails(it) }
    }

    @Composable
    fun mobileRow(viewDetails: (TrainData) -> Unit)  {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(vertical = 10.dp))
                .clickable(interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current,
                    role  = Role.Button,
                    onClickLabel = Strings.get("click_for_details"),
                    onClick = { viewDetails(this@TrainData) }
                )
        ) {
            // TODO)) Add icons
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
                        val trainCategory = category.toShortString()
                        if(category != Category.UNDEFINED) {
                            Text(trainCategory,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Rounded.Train, contentDescription = operator.toString())
                        }
                        Text("$number")
                    }
                }
                Column {
                    Text(
                        station ?: Strings.get("undefined"),
                         fontSize = 20.sp,
                         fontWeight = FontWeight.SemiBold )
                    var timeString: String? = null
                    if(time != null) {
                        timeString = "$time"
                        timeString += getDelayString()
                        if(timeString.contains(Strings.get("cancelled"))) timeString = Strings.get("cancelled")
                    }
                    Text(timeString ?: Strings.get("undefined"))
                }
            }
            Row {
                Column (
                    modifier = Modifier.width(80.dp)
                ) {
                    if(platform.isNullOrEmpty()) platform = null
                    Text(Strings.get("platform"),
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth())
                    Text(platform ?: "-",
                        fontSize = 16.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }

    override fun toString(): String {
        return "$category@$operator #$number@$platform"
    }

}