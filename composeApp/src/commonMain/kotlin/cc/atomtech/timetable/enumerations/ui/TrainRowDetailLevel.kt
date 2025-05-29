package cc.atomtech.timetable.enumerations.ui

/** Handles the UI state of the TrainRow in the Arrivals/Departures section.
 *
 * @since 1.5.0
 */
enum class TrainRowDetailLevel {
    COMPACT,
    DETAILED;

    fun toValue(): Int {
        return when(this) {
            COMPACT -> 0
            DETAILED -> 1
        }
    }

    companion object {
        fun fromValue(value: Int): TrainRowDetailLevel {
            return when(value) {
                0 -> COMPACT
                1 -> DETAILED
                else -> COMPACT
            }
        }
    }
}