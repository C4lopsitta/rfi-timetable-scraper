package cc.atomtech.timetable.models.rfi

import cc.atomtech.timetable.enumerations.StationOperator
import cc.atomtech.timetable.models.utilities.MatchedStation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Some base common data across various station providers. Will be extensively used in the future, once
 * multiple Railway Operators will be added
 *
 * @since 1.5.0
 * @author Simone Robaldo
 */
@Serializable
data class StationBaseData (
    val id: Int,
    val name: String,
    @SerialName("stationCountry")
    val stationOperator: StationOperator,
    var isBookmarked: Boolean = false
) {
    companion object {
        /**
         * Handles the conversion of a [MatchedStation] into an incomplete instance of [StationBaseData].
         *
         * @see [MatchedStation]
         */
        fun fromMatchedStation(station: MatchedStation): StationBaseData {
            return StationBaseData(
                id = station.rfiId?.toInt() ?: 0,
                name = station.rfiName ?: "",
                stationOperator = StationOperator.RFI_IT
            )
        }
    }
}
