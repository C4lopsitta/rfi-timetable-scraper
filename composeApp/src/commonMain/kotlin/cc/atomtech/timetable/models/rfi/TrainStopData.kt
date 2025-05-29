package cc.atomtech.timetable.models.rfi

/**
 * Contains the basic data of a stop in a train's trip.
 *
 * @see [TrainData]
 */
data class TrainStopData(
    val name: String,
    val time: String,
    val isCurrentStop: Boolean
)
