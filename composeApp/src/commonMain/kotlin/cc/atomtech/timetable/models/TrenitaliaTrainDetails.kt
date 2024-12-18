package cc.atomtech.timetable.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@Serializable
data class TrenitaliaTrainDetails @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("tipoTreno") val trainType: String?,
    @JsonNames("fermate") val stops: List<TrenitaliaTrainDetailsStopItem>?,
    @JsonNames("codiceCliente") val userCode: Int?,
    // fermateSoppresse
    @JsonNames("dataPartenzaTreno") val departureDate: Int?,
    @JsonNames("stazioneUltimoRilevamento") val lastUpdateStation: String?,
    @JsonNames("idDestinazione") val destinationStationId: String?,
    @JsonNames("idOrigine") val originStationId: String?,
    // cambiNumero
    @JsonNames("orientamento") val orientation: String,
    @JsonNames("descOrientamento") val orientationDescription: List<String>?,
    @JsonNames("motivoRitardoPrevalente") val delayReason: String?,
    @JsonNames("materiale_label") val stockLabel: String?,
    @JsonNames("numeroTreno") val trainNumber: Int?,
    @JsonNames("categoria") val category: String?,
    @JsonNames("categoriaDescrizione") val categoryDescription: String?,
    @JsonNames("destinazione") val destination: String?,
    @JsonNames("origine") val origin: String?,
    @JsonNames("destinazioneEstera") val internationalDestination: String?,
    @JsonNames("origineEstera") val internationalOrigin: String?,
    @JsonNames("circolante") val isRunning: Boolean?,
    @JsonNames("subTitle") val subtitle: String?,
    @JsonNames("inStazione") val isInStation: Boolean?,
    @JsonNames("nonPartito") val hasYetToDepart: Boolean?,
    // provvedimento
    // hacambinnumero
    // riprogrammazione
    @JsonNames("orarioPartenza") val departureTime: Int?,
    @JsonNames("orarioArrivo") val arrivalTime: Int?,
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
    @JsonNames("stazione") val stationName: String?,
    @JsonNames("id") val stationId: String?,
    // listaCorrispondenze
    @JsonNames("programmata") val scheduledTime: Int?,
    @JsonNames("effettiva") val actualTime: Int?,
    @JsonNames("ritardo") val delay: Int?,
    @JsonNames("ritardoPartenza") val departureDelay: Int?,
    @JsonNames("ritardoArrivo") val arrivalDelay: Int?,
    @JsonNames("partenza_teorica") val expectedDeparture: Int?,
    @JsonNames("arrivo_teorica") val expectedArrival: Int?,
    @JsonNames("partenzaReale") val actualDeparture: Int?,
    @JsonNames("arrivoReale") val actualArrival: Int?,
    @JsonNames("progressivo") val index: Int?,
    @JsonNames("binarioEffettivoPartenzaDescrizione") val actualPlatform: String?,
    @JsonNames("binarioProgrammatoPartenzaDescrizione") val expectedPlatform: String?,
    @JsonNames("materiale_label") val stockLabel: String?,
    @JsonNames("tipoFermata") val stopType: String?,
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
