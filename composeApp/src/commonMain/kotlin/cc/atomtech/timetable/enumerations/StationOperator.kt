package cc.atomtech.timetable.enumerations

/**
 * @since 1.5.0
 */
enum class StationOperator {
    RFI_IT;

    override fun toString(): String {
        return when(this) {
            RFI_IT -> "RFI"
        }
    }
}