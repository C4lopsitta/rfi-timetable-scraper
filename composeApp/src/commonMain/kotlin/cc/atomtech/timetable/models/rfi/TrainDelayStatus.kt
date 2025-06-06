package cc.atomtech.timetable.models.rfi

import cc.atomtech.timetable.StringRes

/**
 * Enumeration of the Delay status of a train.
 *
 * @see [TrainDelayStatus]
 */
enum class TrainStatus {
    RUNNING,
    DELAYED,
    CANCELLED;
}

/**
 * Handles the Delay and running status of a train.
 *
 * @see [TrainStatus]
 */
data class TrainDelayStatus(
    val delay: Int,
    val status: TrainStatus
) {
    fun toString(padded: Boolean = true): String {
        return (if(padded) " " else "") + when(status) {
            TrainStatus.RUNNING -> when(delay) {
                0 -> ""
                else -> "+$delay ${StringRes.get("minutes")}"
            }
            TrainStatus.DELAYED -> StringRes.get("delayed")
            TrainStatus.CANCELLED -> StringRes.get("cancelled")
        }
    }
}
