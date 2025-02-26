package cc.atomtech.timetable.models.trenitalia

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RestEasyTrainData @OptIn(ExperimentalSerializationApi::class) constructor(
    @SerialName("tipoTreno") val trainType: String?,
    @SerialName("fermate") val stops: List<TrenitaliaTrainDetailsStopItem>?,
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

@Serializable
data class TrenitaliaTrainDetailsStopItem @OptIn(ExperimentalSerializationApi::class) constructor(
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
            TrenitaliaTrainDetailsStopItem: {
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
