package cc.atomtech.rfi_timetable.enumerations

enum class Operator() {
    UNDEFINED,
    TRENITALIA,
    TRENORD,
    TRENITALIA_TPER,
    ITALO_NTV,
    ARENAWAYS;

    companion object {
        fun fromString(operatorName: String?): Operator {
            if(operatorName == null) return UNDEFINED
            if(operatorName.contains("TPER"))
            return TRENITALIA_TPER
            if(operatorName.contains("TRENITALIA") ||
            operatorName.contains("FRECCIAROSSA") ||
            operatorName.contains("FRECCIARGENTO") ||
            operatorName.contains("FRECCIABIANCA"))
            return TRENITALIA
            if(operatorName.contains("Trenord"))
            return TRENORD
            if(operatorName.contains("ITALO"))
            return ITALO_NTV
            return UNDEFINED
        }
    }

    override fun toString(): String {
        return when(this) {
            UNDEFINED -> "Unknown"
            TRENITALIA -> "Trenitalia"
            TRENORD -> "Trenord"
            TRENITALIA_TPER -> "Trenitalia TPER"
            ITALO_NTV -> "NTV Italo"
            ARENAWAYS -> "Arenaways"
        }
    }

}