package cc.atomtech.timetable.models.trenitalia

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CercaTrenoData(
    @SerialName("numeroTreno") val number: String,
    @SerialName("codLocOrig") val originCode: String,
    @SerialName("descLocOrig") val originName: String,
    @SerialName("dataPartenza") val departureDate: String,
    @SerialName("corsa") val corsa: String,
    @SerialName("h24") val h24: Boolean,
    @SerialName("tipo") val type: String,
    @SerialName("millisDataPartenza") val departureTimestamp: String,
    // val formatDataPartenza: Any
)
