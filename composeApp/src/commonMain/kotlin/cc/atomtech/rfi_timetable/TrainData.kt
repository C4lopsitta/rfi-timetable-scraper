package cc.atomtech.rfi_timetable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

class TrainData(
    private val operatorName: String?,
    private val number: String?,
    private val category: String?,
    private var platform: String?,
    private val delay: Int,
    private val station: String? = null,
    private val time: String? = null,
    private val details: String? = null) {

    @Composable
    fun mobileRow()  {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(PaddingValues(vertical = 10.dp))
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
                        modifier = Modifier.width(52.dp)
                    ) {
                        Icon(Icons.Rounded.Train, contentDescription = operatorName)
                        Text("$number")
                    }
                }
                Column {
                    Text(
                        station ?: "Undefined",
                         fontSize = 20.sp,
                         fontWeight = FontWeight.SemiBold )
                    var timeString: String? = null
                    if (time != null) {
                        timeString = "$time"
                        if(delay > 0) {
                            if(delay == Int.MAX_VALUE) {
                                timeString += " DELAYED"
                            } else {
                                timeString += "\t+$delay minutes"
                            }
                        }
                    }

                    Text(timeString ?: "Undefined o'clock")
                }
            }
            Row {
                Column (
                    modifier = Modifier.width(80.dp)
                ) {
                    if(platform.isNullOrEmpty()) platform = null
                    Text("Platform",
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth())
                    Text(platform ?: "-",
                        fontSize = 16.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth())
                }
                IconButton(
                    onClick = {},
                ) {
                    Icon(Icons.Rounded.Info, contentDescription = "Details for train $number")
                }
            }
        }
    }

    override fun toString(): String {
        return "$category@$operatorName #$number@$platform"
    }

}