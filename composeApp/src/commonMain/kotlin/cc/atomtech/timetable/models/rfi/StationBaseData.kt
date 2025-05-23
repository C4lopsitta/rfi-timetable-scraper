package cc.atomtech.timetable.models.rfi

import cc.atomtech.timetable.enumerations.StationOperator
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StationBaseData (
    val id: Int,
    val name: String,
    @SerialName("stationCountry")
    val stationOperator: StationOperator,
    var isBookmarked: Boolean = false
)
