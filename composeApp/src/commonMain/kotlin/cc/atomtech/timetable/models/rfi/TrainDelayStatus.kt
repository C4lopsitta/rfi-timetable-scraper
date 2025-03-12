package cc.atomtech.timetable.models.rfi

import cc.atomtech.timetable.StringRes

enum class TrainStatus {
    RUNNING,
    DELAYED,
    CANCELLED;
}

data class TrainDelayStatus(
    val delay: Int,
    val status: TrainStatus
) {
    fun toString(padded: Boolean = true): String {
        return when(status) {
            TrainStatus.RUNNING -> when(delay) {
                0 -> ""
                else -> "${if(padded) " " else ""}+$delay ${StringRes.get("minutes")}"
            }
            TrainStatus.DELAYED -> StringRes.get("delayed")
            TrainStatus.CANCELLED -> StringRes.get("cancelled")
        }
    }

    companion object {
        fun fromScraper(): TrainDelayStatus {
            throw NotImplementedError()
        }
    }
}
