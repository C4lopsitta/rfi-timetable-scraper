package cc.atomtech.timetable.enumerations.preferences

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