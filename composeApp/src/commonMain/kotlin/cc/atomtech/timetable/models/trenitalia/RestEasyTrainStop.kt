package cc.atomtech.timetable.models.trenitalia

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Data class for containing deserialized stops from the CercaTreno API
 *
 * @see RestEasyTrainData
 * @see cc.atomtech.timetable.apis.TrenitaliaRestEasy
 * @since 1.5.0
 */
@Serializable
data class RestEasyTrainStop (
    val orientamento: String?,
    // kcNumTreno
    @SerialName("stazione") val stationName: String?,
    @SerialName("id") val stationId: String?,
    // listaCorrispondenze
    @SerialName("programmata") val scheduledTime: Long?,
    @SerialName("effettiva") val actualTime: Long?,
    @SerialName("ritardo") val delay: Long?,
    @SerialName("ritardoPartenza") val departureDelay: Long?,
    @SerialName("ritardoArrivo") val arrivalDelay: Long?,
    @SerialName("partenza_teorica") val expectedDeparture: Long? = null,
    @SerialName("arrivo_teorica") val expectedArrival: Long? = null,
    @SerialName("partenzaReale") val actualDeparture: Long? = null,
    @SerialName("arrivoReale") val actualArrival: Long? = null,
    @SerialName("progressivo") val index: Long?,
    @SerialName("binarioEffettivoPartenzaDescrizione") val actualPlatform: String?,
    @SerialName("binarioProgrammatoPartenzaDescrizione") val expectedPlatform: String?,
    @SerialName("materiale_label") val stockLabel: String?,
    @SerialName("tipoFermata") val stopType: String?,
) {
    override fun toString(): String {
        return """
            RestEasyTrainStop: {
                stationName: $stationName
                stationId: $stationId
                scheduledTime: $scheduledTime
                actualTime: $actualTime
                delay: $delay
                departureDelay: $departureDelay
                arrivalDelay: $arrivalDelay
                expectedDeparture: $expectedDeparture
                expectedArrival: $expectedArrival
                actualDeparture: $actualDeparture
                actualArrival: $actualArrival
                index: $index
                expectedPlatform: $expectedPlatform
                actualPlatform: $actualPlatform
                stockLabel: $stockLabel
                stopType: $stopType
            }
        """.trimIndent()
    }
}
