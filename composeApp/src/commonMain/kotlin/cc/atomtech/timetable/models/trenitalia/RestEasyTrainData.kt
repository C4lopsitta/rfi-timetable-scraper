package cc.atomtech.timetable.models.trenitalia

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Data class for containing a deserialized train from the CercaTreno API
 *
 * @see RestEasyTrainStop
 * @see cc.atomtech.timetable.apis.TrenitaliaRestEasy
 * @since 1.5.0
 */

@Serializable
data class RestEasyTrainData (
    @SerialName("tipoTreno") val trainType: String?,
    @SerialName("fermate") val stops: List<RestEasyTrainStop>?,
    @SerialName("codiceCliente") val userCode: Long?,
    // fermateSoppresse
    @SerialName("dataPartenzaTreno") val departureDate: Long?,
    @SerialName("stazioneUltimoRilevamento") val lastUpdateStation: String?,
    @SerialName("idDestinazione") val destinationStationId: String?,
    @SerialName("idOrigine") val originStationId: String?,
    // cambiNumero
    @SerialName("orientamento") val orientation: String?,
    @SerialName("descOrientamento") val orientationDescription: List<String>?,
    @SerialName("motivoRitardoPrevalente") val delayReason: String?,
    @SerialName("materiale_label") val stockLabel: String?,
    @SerialName("numeroTreno") val trainNumber: Long?,
    @SerialName("categoria") val category: String?,
    @SerialName("categoriaDescrizione") val categoryDescription: String?,
    @SerialName("destinazione") val destination: String?,
    @SerialName("origine") val origin: String?,
    @SerialName("destinazioneEstera") val internationalDestination: String?,
    @SerialName("origineEstera") val internationalOrigin: String?,
    @SerialName("circolante") val isRunning: Boolean?,
    @SerialName("subTitle") val subtitle: String?,
    @SerialName("inStazione") val isInStation: Boolean?,
    @SerialName("nonPartito") val hasYetToDepart: Boolean?,
    // provvedimento
    // hacambinnumero
    // riprogrammazione
    @SerialName("orarioPartenza") val departureTime: Long?,
    @SerialName("orarioArrivo") val arrivalTime: Long?,
    // statoTreno
    // all comp.. values
) {
    override fun toString(): String {
        return """
            TrenitaliaTrainDetails: {
                trainType: $trainType
                stops: $stops
                userCode: $userCode
                departureDate: $departureDate
                lastUpdateStation: $lastUpdateStation
                destinationStationId: $destinationStationId
                originStationId: $originStationId
                orientation: $orientation
                orientationDescription: $orientationDescription
                delayReason: $delayReason
                stockLabel: $stockLabel
                trainNumber: $trainNumber
                category: $category
                categoryDescription: $categoryDescription
                destination: $destination
                origin: $origin
                internationalDestination: $internationalDestination
                internationalOrigin: $internationalOrigin
                isRunning: $isRunning
                subtitle: $subtitle
                isInStation: $isInStation
                hasYetToDepart: $hasYetToDepart
                departureTime: $departureTime
                arrivalTime: $arrivalTime
            }
        """.trimIndent()
    }
}
