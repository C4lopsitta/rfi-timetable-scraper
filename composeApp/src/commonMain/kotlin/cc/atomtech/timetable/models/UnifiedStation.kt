package cc.atomtech.timetable.models

import kotlinx.serialization.Serializable


@Serializable
data class UnifiedStation(
    val displayName: String?,
    val rfiId: Int,
//    val rfiPrmId: Int,
    val trenitaliaId: String,
    val rfiName: String,
//    val rfiPrmName: String,
    val trenitaliaName: String,
    val trenitaliaShortenedName: String?,
    val trenitaliaLabel: String?
) {

}
