package cc.atomtech.timetable.models.rfi

import kotlinx.serialization.Serializable

@Serializable
data class StationBaseData (
    val id: Int,
    val name: String
)
