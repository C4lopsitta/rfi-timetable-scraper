package cc.atomtech.timetable.enumerations

/** Enumeration of any (known) train Category.
 *
 * Contains any category, from Regional, to Freccie, including SFM, EC, IC and others.
 *
 */
enum class Category {
    UNDEFINED,
    REG_VEL,
    REG,
    SFM_TO_1,
    SFM_TO_2,
    SFM_TO_3,
    SFM_TO_4,
    SFM_TO_6,
    SFM_TO_7,
    SFM_TO_A,
    IC,
    ICN,
    EUROCITY,
    SFS_MI_1,
    SFS_MI_2,
    SFS_MI_3,
    SFS_MI_4,
    SFS_MI_5,
    SFS_MI_6,
    SFS_MI_7,
    SFS_MI_8,
    SFS_MI_9,
    SFS_MI_10,
    SFS_MI_11,
    SFS_MI_12,
    SFS_MI_13,
    SFS_MI_14,
    TRENORD,
    BUS,
    REGIO_EXP,
    MALP_EXP,
    FR,
    FAG,
    FB,
    ITALO,
    STORICO;

    companion object {
        fun fromString(category: String?, operatorName: String?): Category {
            if(category == null) return UNDEFINED
            if(category.contains("VELOCE"))
                return REG_VEL
            if(category.contains("REGIONALE"))
                return REG
            if(category.contains("Servizio Ferroviario Metropolitano", ignoreCase = true) || category.startsWith("Categoria SFM linea", ignoreCase = true)) {
                val lineNumber = category
                    .trimEnd { it.isWhitespace() }
                    .last()

                return when(lineNumber) {
                    '1' -> SFM_TO_1
                    '2' -> SFM_TO_2
                    '3' -> SFM_TO_3
                    '4' -> SFM_TO_4
                    '6' -> SFM_TO_6
                    '7' -> SFM_TO_7
                    'A' -> SFM_TO_A
                    'a' -> SFM_TO_A
                    else -> UNDEFINED
                }
            }
            if(category.contains("INTERCITY")) {
                if (category.contains("NOTTE"))
                    return ICN
                return IC
            }
            if(category.contains("Trenord"))
                return TRENORD
            if(category.contains("SUBURBANO")) {
                val line = category.substringAfter("SUBURBANO ")
                return when (line) {
                    "1" -> SFS_MI_1
                    "2" -> SFS_MI_2
                    "3" -> SFS_MI_3
                    "4" -> SFS_MI_4
                    "5" -> SFS_MI_5
                    "6" -> SFS_MI_6
                    "7" -> SFS_MI_7
                    "8" -> SFS_MI_8
                    "9" -> SFS_MI_9
                    "10" -> SFS_MI_10
                    "11" -> SFS_MI_11
                    "12" -> SFS_MI_12
                    "13" -> SFS_MI_13
                    "14" -> SFS_MI_14
                    else -> UNDEFINED
                }
            }
            if(category.contains("AUTOCORSA"))
                return BUS
            if(category.contains("REGIO EXPRESS"))
                return REGIO_EXP
            if(category.contains("MALPENSA EXPRESS"))
                return MALP_EXP
            if(category.contains("EUROCITY"))
                return EUROCITY
            if(category.contains("TRENO STORICO"))
                return STORICO
            if(operatorName == "FRECCIAROSSA")
                return FR
            if(operatorName == "FRECCIARGENTO")
                return FAG
            if(operatorName == "FRECCIABIANCA")
                return FB
            if(operatorName == "ITALO")
                return ITALO
            return UNDEFINED
        }
    }

    /**
     * Returns an human readable [String] to display to the User. If a complete name is needed, check [toString]
     *
     * @see [toString]
     */
    fun toShortString(): String {
        return when(this) {
            REG -> "REG"
            REG_VEL -> "RV"
            IC -> "IC"
            ICN -> "ICN"
            EUROCITY -> "EC"
            FR -> "FR"
            FAG -> "FAg"
            FB -> "FB"
            ITALO -> "ITA"
            MALP_EXP -> "MPX"
            REGIO_EXP -> "REX"
            BUS -> "BUS"
            TRENORD -> "TN"
            SFM_TO_1 -> "SFM1"
            SFM_TO_2 -> "SFM2"
            SFM_TO_3 -> "SFM3"
            SFM_TO_4 -> "SFM4"
            SFM_TO_6 -> "SFM6"
            SFM_TO_7 -> "SFM7"
            SFM_TO_A -> "SFMA"
            SFS_MI_1 -> "S1"
            SFS_MI_2 -> "S2"
            SFS_MI_3 -> "S3"
            SFS_MI_4 -> "S4"
            SFS_MI_5 -> "S5"
            SFS_MI_6 -> "S6"
            SFS_MI_7 -> "S7"
            SFS_MI_8 -> "S8"
            SFS_MI_9 -> "S9"
            SFS_MI_10 -> "S10"
            SFS_MI_11 -> "S11"
            SFS_MI_12 -> "S12"
            SFS_MI_13 -> "S13"
            SFS_MI_14 -> "S14"
            STORICO -> "ST"
            else -> ""
        }
    }

    /**
     * Returns the complete Category name instead of a shortened string
     *
     * @see [toShortString]
     */
    override fun toString(): String {
        return when(this) {
            REG -> "Regionale"
            REG_VEL -> "Regionale Veloce"
            IC -> "Intercity"
            ICN -> "Intercity Notte"
            EUROCITY -> "Eurocity"
            FR -> "Frecciarossa"
            FAG -> "Frecciargento"
            FB -> "Frecciabianca"
            ITALO -> "Italo"
            MALP_EXP -> "Malpensa Express"
            REGIO_EXP -> "Regio Express"
            BUS -> "Bus"
            TRENORD -> "Trenord"
            SFM_TO_1 -> "SFM Linea 1"
            SFM_TO_2 -> "SFM Linea 2"
            SFM_TO_3 -> "SFM Linea 3"
            SFM_TO_4 -> "SFM Linea 4"
            SFM_TO_6 -> "SFM Linea 6"
            SFM_TO_7 -> "SFM Linea 7"
            SFM_TO_A -> "SFM Linea A"
            SFS_MI_1 -> "Suburbano 1"
            SFS_MI_2 -> "Suburbano 2"
            SFS_MI_3 -> "Suburbano 3"
            SFS_MI_4 -> "Suburbano 4"
            SFS_MI_5 -> "Suburbano 5"
            SFS_MI_6 -> "Suburbano 6"
            SFS_MI_7 -> "Suburbano 7"
            SFS_MI_8 -> "Suburbano 8"
            SFS_MI_9 -> "Suburbano 9"
            SFS_MI_10 -> "Suburbano 10"
            SFS_MI_11 -> "Suburbano 11"
            SFS_MI_12 -> "Suburbano 12"
            SFS_MI_13 -> "Suburbano 13"
            SFS_MI_14 -> "Suburbano 14"
            STORICO -> "Storico"
            else -> "Undefined"
        }
    }
}