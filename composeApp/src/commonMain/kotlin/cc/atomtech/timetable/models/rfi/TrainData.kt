package cc.atomtech.timetable.models.rfi

import cc.atomtech.timetable.enumerations.Category
import cc.atomtech.timetable.enumerations.Operator
import cc.atomtech.timetable.enumerations.TrainType
import cc.atomtech.timetable.apis.scrapers.RfiPartenzeArrivi

/**
 * Data Class used by [RfiPartenzeArrivi] to return trains scraped from the IecHub Web Interface.
 *
 * @see [TrainStopData]
 * @see [TrainDelayStatus]
 */
data class TrainData(
    val operator: Operator,
    val operatorName: String? = null,
    val category: Category,
    val categoryName: String? = null,
    val number: String?,
    val platform: String?,
    val delay: TrainDelayStatus,
    val station: String? = null,
    val time: String? = null,
    val stops: List<TrainStopData>,
    val details: String? = null,
    val trainType: TrainType
) {
    override fun toString(): String {
        return "[rfi.TrainData] #$number ($operator $category) at Platform $platform"
    }
}
