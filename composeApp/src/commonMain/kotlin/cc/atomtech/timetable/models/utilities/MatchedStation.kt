package cc.atomtech.timetable.models.utilities

import kotlinx.serialization.Serializable

import cc.atomtech.timetable.models.viewmodels.StationListViewModel

/**
 * Data Class related to the [StationListViewModel]'s JSON loader.
 *
 * @see [StationListViewModel.loadFile]
 * @author Simone Robaldo
 * @since 1.5.0
 */
@Serializable
data class MatchedStation(
    val rfiId: String? = null,
    val trenitaliaId: String? = null,
    val rfiName: String? = null,
    val trenitaliaName: String? = null,
    val trenitaliaShortenedName: String? = null,
    val trenitaliaLabel: String? = null
) {
    override fun toString(): String {
        return """
            MatchedStation(
                rfiId=$rfiId,
                trenitaliaId=$trenitaliaId,
                rfiName=$rfiName,
                trenitaliaName=$trenitaliaName,
                trenitaliaShortenedName=$trenitaliaShortenedName,
                trenitaliaLabel=$trenitaliaLabel
            )
        """.trimIndent()
    }
}
