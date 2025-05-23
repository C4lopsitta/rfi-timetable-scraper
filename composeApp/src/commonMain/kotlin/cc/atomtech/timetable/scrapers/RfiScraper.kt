package cc.atomtech.timetable.scrapers

import cc.atomtech.timetable.enumerations.Category
import cc.atomtech.timetable.enumerations.Operator
import cc.atomtech.timetable.models.OldStationModel
import cc.atomtech.timetable.models.TimetableData
import cc.atomtech.timetable.models.TimetableState
import cc.atomtech.timetable.models.OldRfiTrainData
import cc.atomtech.timetable.models.TrainStop
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.select.Elements

object HtmlTagsIdNames {
    const val STATION_NAME = "nomeStazioneId"
    const val TABLE = "bodyTabId"
    const val STATION_INFO = "Informazioni di stazione"

    const val OPERATOR_FIELD = "RVettore"
    const val CATEGORY_FIELD = "RCategoria"
    const val NUMBER_FIELD = "RTreno"
    const val STATION_FIELD = "RStazione"
    const val TIME_FIELD = "ROrario"
    const val DELAY_FIELD = "RRitardo"
    const val PLATFORM_FIELD = "RBinario"
    const val DETAILS_BUTTON_FIELD = "RDettagli"
    const val DETAILS_POPUP_ELEMENT_CLASS = "FermateSuccessivePopupStyle"
    const val STATIONS_LIST = "ElencoLocalita"
}

@Deprecated("Deprecated since v.1.5.0", ReplaceWith("cc.atomtech.apis.scrapers.RfiPartenzeArrivi"))
object RfiScraper {
    private const val stationsUrl = "https://iechub.rfi.it/ArriviPartenze/en/ArrivalsDepartures/Home"
    private const val baseUrl = "https://iechub.rfi.it/ArriviPartenze/ArrivalsDepartures/Monitor"
    private const val baseQueryDepartures = "?Arrivals=False&PlaceId="
    private const val baseQueryArrivals = "?Arrivals=True&PlaceId="

    private fun String.stationName(): String {
        return this.lowercase()
            .replace("''''", "'")
            .split(" ")
            .joinToString(" ") { word ->
                word.split(".")
                    .joinToString(".") { part ->
                        part.replaceFirstChar { it.uppercaseChar() }
                    }
            }
            .split(" ")
            .joinToString(" ") { word ->
                word.split("-")
                    .joinToString("-") { part ->
                        part.replaceFirstChar { it.uppercaseChar() }
                    }
            }
            .replace("-", " - ")
    }

    suspend fun getStations(): ArrayList<OldStationModel> {
        val stations = Ksoup.parseGetRequest(url = stationsUrl)

        val stationList = stations.body().getElementById(HtmlTagsIdNames.STATIONS_LIST)

        val stationsList: ArrayList<OldStationModel> = arrayListOf()

        stationList?.getElementsByTag("option")?.forEach { option ->
            stationsList.add(OldStationModel(option.html().stationName(), option.value().toInt()))
        }

        return stationsList
    }

    private fun tableToTrainList(tableRows: Elements?): List<OldRfiTrainData> {
        val trains: ArrayList<OldRfiTrainData> = arrayListOf()
        tableRows?.forEach { tr ->
            if (tr.children()[0].tagName() != "th") {
                if (tr.id().isNotEmpty()) {

                    val trainNumber = tr.id()
                    var operator = "UNDEFINED"
                    var category = "UNDEFINED"

                    try {
                        operator =
                            tr.getElementById(HtmlTagsIdNames.OPERATOR_FIELD)!!
                                .children()[0].attribute(
                                "alt"
                            )?.value ?: "UNDEFINED"
                    } catch (e: Exception) {
                        operator = "UNDEFINED"
                    }

                    try {
                        category =
                            tr.getElementById(HtmlTagsIdNames.CATEGORY_FIELD)!!
                                .children()[0].attribute(
                                "alt"
                            )?.value ?: "UNDEFINED"
                    } catch (e: Exception) {
                        category = "UNDEFINED"
                    }

                    val platform =
                        tr.getElementById(HtmlTagsIdNames.PLATFORM_FIELD)!!.children()[0].html()
                    val station =
                        tr.getElementById(HtmlTagsIdNames.STATION_FIELD)!!.children()[0].html()
                    val time =
                        tr.getElementById(HtmlTagsIdNames.TIME_FIELD)!!.html()
                    val delay =
                        tr.getElementById(HtmlTagsIdNames.DELAY_FIELD)!!.html()

                    val stops = arrayListOf<TrainStop>()
                    var moreInformationString = ""

                    if(tr.getElementById(HtmlTagsIdNames.DETAILS_BUTTON_FIELD)?.children()?.size != 0) {
                        val detailsPopupElements =
                            tr.getElementsByClass(HtmlTagsIdNames.DETAILS_POPUP_ELEMENT_CLASS)[0].children()
                        var nextStationsString = ""

                        for(i in 0..<detailsPopupElements.size step 1) {
                            if(detailsPopupElements[i].html() == "Fermate successive")
                                nextStationsString = detailsPopupElements[i+1].html()
                            if(detailsPopupElements[i].html() == "Informazioni")
                                moreInformationString = detailsPopupElements[i+1].html()
                        }

                        if(nextStationsString.isNotEmpty()) {
                            nextStationsString.split("FERMA A: ")[1]
                            nextStationsString.split(" - ").forEach { stop ->
                                var time = stop.split("(")[1]
                                    .removeSuffix(")")
                                    .replace(".", ":")
                                if(time.split(":").size == 1) time = "0$time"
                                stops.add(
                                    TrainStop(
                                        name = stop.split(" (")[0].removePrefix("FERMA A: ").stationName(),
                                        time = time,
                                        isCurrentStop = false
                                    )
                                )
                            }
                        }
                    }

                    var delayMinutes = 0
                    if (delay == "RITARDO") {
                        delayMinutes = Int.MAX_VALUE
                    } else if (delay == "Cancellato") {
                        delayMinutes = Int.MIN_VALUE
                    } else if (delay.isNotEmpty()) {
                        delayMinutes = delay.toInt()
                    }

                    trains.add(
                        OldRfiTrainData(
                            number = trainNumber,
                            operator = Operator.fromString(operator),
                            operatorName = operator,
                            category = Category.fromString(category, operator),
                            platform = platform,
                            station = station.stationName(),
                            time = time,
                            delay = delayMinutes,
                            details = moreInformationString,
                            stops = stops
                        )
                    )
                }
            }
        }

        return trains.toList()
    }

    suspend fun getStationTimetable(stationId: Int): TimetableState {
        val state = reloadStation(stationId)

        return TimetableState(stationName = state.stationName.stationName(),
                         departures = state.departures,
                         arrivals = state.arrivals,
                         stationInfo = state.stationInfo)
    }

    suspend fun reloadStation(stationId: Int): TimetableData {
        val departures = Ksoup.parseGetRequest(url="$baseUrl$baseQueryDepartures${stationId}")
        val arrivals = Ksoup.parseGetRequest(url="$baseUrl$baseQueryArrivals${stationId}")

        val title = departures.body().getElementById(HtmlTagsIdNames.STATION_NAME)?.html() ?: "Error"
        val departuresTableBody = departures.body().getElementById(HtmlTagsIdNames.TABLE)
        val arrivalsTableBody = arrivals.body().getElementById(HtmlTagsIdNames.TABLE)
        val stationInfo = departures.body().getElementById(HtmlTagsIdNames.STATION_INFO)

        val departingTrains: List<OldRfiTrainData> = tableToTrainList(departuresTableBody?.getElementsByTag("tr"))
        val arrivingTrains: List<OldRfiTrainData> = tableToTrainList(arrivalsTableBody?.getElementsByTag("tr"))

        var stationInformation: String? = null
        if(stationInfo?.children() != null) {
            stationInformation = stationInfo.children()[0].html().removePrefix("<div>").removeSuffix("</div>")
        }

        return TimetableData(
            departures = departingTrains,
            arrivals = arrivingTrains,
            stationName = title.stationName(),
            stationInfo = stationInformation
        )
    }
}