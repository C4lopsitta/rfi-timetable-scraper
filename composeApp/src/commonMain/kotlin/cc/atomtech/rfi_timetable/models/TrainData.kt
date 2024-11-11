package cc.atomtech.rfi_timetable.models

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
import androidx.navigation.NavHost
import androidx.navigation.NavHostController

class TrainData(
    private val operatorName: String?,
    val number: String?,
    private val category: String?,
    private var platform: String?,
    private val delay: Int,
    private val station: String? = null,
    private val time: String? = null,
    private val details: String? = null) {

    fun getTrainCategoryRowString(): String? {
        if(category == null) return null
        if(category.contains("VELOCE"))
            return "RV"
        if(category.contains("REGIONALE"))
            return "REG"
        if(category.contains("Servizio Ferroviario Metropolitano")) {
            val lineNumber = category.last()
            return "SFM$lineNumber"
        }
        if(category.contains("INTERCITY")) {
            if (category.contains("NOTTE"))
                return "ICN"
            return "IC"
        }
        if(category.contains("Trenord"))
            return "TN"
        if(category.contains("SUBURBANO"))
            return category.substringAfter("SUBURBANO ")
        if(category.contains("AUTOCORSA"))
            return "BUS"
        if(category.contains("REGIO EXPRESS"))
            return "RE"
        if(category.contains("MALPENSA EXPRESS"))
            return "MPX"
        if(category.contains("EUROCITY"))
            return "EC"
        if(operatorName == "FRECCIAROSSA")
            return "FR"
        if(operatorName == "ITALO")
            return "ITA"
        return null
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
                    onClickLabel = "Click to open details",
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
                        modifier = Modifier.width(52.dp)
                    ) {
                        val trainCategory = getTrainCategoryRowString()
                        if(trainCategory != null) {
                            Text(trainCategory,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Rounded.Train, contentDescription = operatorName)
                        }
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
                        when (delay) {
                            Int.MAX_VALUE -> {
                                timeString += " DELAYED"
                            }
                            Int.MIN_VALUE -> {
                                timeString = "CANCELLED"
                            }
                            0 -> {  }
                            else -> {
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
            }
        }
    }

    override fun toString(): String {
        return "$category@$operatorName #$number@$platform"
    }

}