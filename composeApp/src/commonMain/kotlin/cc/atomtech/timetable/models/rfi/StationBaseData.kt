package cc.atomtech.timetable.models.rfi

import cc.atomtech.timetable.enumerations.StationCountry
import kotlinx.serialization.Serializable

@Serializable
data class StationBaseData (
    val id: Int,
    val name: String,
    val stationCountry: StationCountry,
    var isBookmarked: Boolean = false
)
