package cc.atomtech.timetable.enumerations

enum class StationCountry {
    RFI_IT;

    override fun toString(): String {
        return when(this) {
            RFI_IT -> "RFI"
        }
    }
}