package cc.atomtech.timetable.models

import kotlinx.serialization.Serializable

/**
 * This class, currently unused, relates to the *Matched Stations* between RFI and Trenitalia in the `json`.
 */
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
