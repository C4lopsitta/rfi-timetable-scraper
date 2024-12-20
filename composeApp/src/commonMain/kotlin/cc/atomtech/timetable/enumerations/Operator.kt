package cc.atomtech.timetable.enumerations

enum class Operator {
    UNDEFINED,
    TRENITALIA,
    TRENORD,
    TRENITALIA_TPER,
    ITALO_NTV,
    ARENAWAYS,
    SAD,
    SNCF,
    TRENINO_TRASPORTI,
    FONDAZIONE_FS,
    FERROVIE_GARGANO;

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
            if(operatorName.contains("SAD"))
                return SAD
            if(operatorName.contains("SNCF"))
                return SNCF
            if(operatorName.contains("Trentino Trasporti"))
                return TRENINO_TRASPORTI
            if(operatorName.contains("FONDAZIONE FS"))
                return FONDAZIONE_FS
            if(operatorName.contains("Ferrovie del Gargano"))
                return FERROVIE_GARGANO
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
            SAD -> "Società Autobus Alto Adige"
            SNCF -> "SNCF"
            TRENINO_TRASPORTI -> "Trentino Trasporti"
            FONDAZIONE_FS -> "Fondazione FS"
            FERROVIE_GARGANO -> "Ferrovie del Gargano"
        }
    }

}