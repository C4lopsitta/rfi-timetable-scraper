package cc.atomtech.rfi_timetable

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class TrainData(
    private val operatorName: String?,
    private val number: Int?,
    private val category: String?,
    private val platform: Int,
    private val delay: Int,
    private val station: String? = null,
    private val time: String? = null,
    private val details: String? = null) {


    @Composable
    fun build()  {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // TODO)) Add icons
            Icon(Icons.Rounded.Train, contentDescription = operatorName)
            Column {
                Text(station ?: "Undefined")
                Text(time ?: "Undefined o'clock")
                Text("Train number $number")
            }
            IconButton(onClick = {}) {
                Icon(Icons.Rounded.Info, contentDescription = "Details for train $number")
            }
        }
    }

    override fun toString(): String {
        return "$category@$operatorName #$number@$platform"
    }

}