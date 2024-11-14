package cc.atomtech.timetable

import cc.atomtech.timetable.enumerations.Category
import cc.atomtech.timetable.enumerations.Operator
import cc.atomtech.timetable.models.Station
import cc.atomtech.timetable.models.Stop
import cc.atomtech.timetable.models.TimetableState
import cc.atomtech.timetable.models.TrainData
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.select.Elements
import java.util.Locale

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
    const val STAITONS_LIST = "ElencoLocalita"
}

object RfiScraper {
    private const val stationsUrl = "https://iechub.rfi.it/ArriviPartenze/en/ArrivalsDepartures/Home"
    private const val baseUrl = "https://iechub.rfi.it/ArriviPartenze/ArrivalsDepartures/Monitor"
    private const val baseQueryDepartures = "?Arrivals=False&PlaceId="
    private const val baseQueryArrivals = "?Arrivals=True&PlaceId="

    private fun String.stationName(): String {
        this.lowercase(Locale.getDefault())
        return this.lowercase().split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercaseChar() }
        }
    }

    suspend fun getStations(): List<Station> {
        val stations = Ksoup.parseGetRequest(url = stationsUrl)

        val stationList = stations.body().getElementById(HtmlTagsIdNames.STAITONS_LIST)

        val stationsList: ArrayList<Station> = arrayListOf()

        stationList?.getElementsByTag("option")?.forEach { option ->
            stationsList.add(Station(option.html().stationName(), option.value().toInt()))
        }

        return stationsList.toList()
    }

    private fun tableToTrainList(tableRows: Elements?): List<TrainData> {
        val trains: ArrayList<TrainData> = arrayListOf()
        tableRows?.forEach { tr ->
            if (tr.children()[0].tagName() != "th") {
                if (tr.id().isNotEmpty()) {
                    println(tr.id())

                    val trainNumber = tr.id()

                    val operator =
                        tr.getElementById(HtmlTagsIdNames.OPERATOR_FIELD)!!
                            .children()[0].attribute(
                            "alt"
                        )?.value ?: "UNDEFINED"
                    val category =
                        tr.getElementById(HtmlTagsIdNames.CATEGORY_FIELD)!!
                            .children()[0].attribute(
                            "alt"
                        )?.value ?: "UNDEFINED"
                    val platform =
                        tr.getElementById(HtmlTagsIdNames.PLATFORM_FIELD)!!.children()[0].html()
                    val station =
                        tr.getElementById(HtmlTagsIdNames.STATION_FIELD)!!.children()[0].html()
                    val time =
                        tr.getElementById(HtmlTagsIdNames.TIME_FIELD)!!.html()
                    val delay =
                        tr.getElementById(HtmlTagsIdNames.DELAY_FIELD)!!.html()

                    val stops = arrayListOf<Stop>()
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
                                stops.add(Stop(stop.split(" (")[0].removePrefix("FERMA A: "), stop.split("(")[1].removeSuffix(")")))
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
                        TrainData(
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
        val departures = Ksoup.parseGetRequest(url="${baseUrl}${baseQueryDepartures}${stationId}")
        val arrivals = Ksoup.parseGetRequest(url="${baseUrl}${baseQueryArrivals}${stationId}")

        val title = departures.body().getElementById(HtmlTagsIdNames.STATION_NAME)?.html() ?: "Error"
        val departuresTableBody = departures.body().getElementById(HtmlTagsIdNames.TABLE)
        val arrivalsTableBody = arrivals.body().getElementById(HtmlTagsIdNames.TABLE)
        val stationInfo = departures.body().getElementById(HtmlTagsIdNames.STATION_INFO)

        val departingTrains: List<TrainData> = tableToTrainList(departuresTableBody?.getElementsByTag("tr"))
        val arrivingTrains: List<TrainData> = tableToTrainList(arrivalsTableBody?.getElementsByTag("tr"))

        var stationInformation: String? = null
        if(stationInfo?.children() != null) {
            stationInformation = stationInfo.children()[0].html().removePrefix("<div>").removeSuffix("</div>")
        }

        return TimetableState(stationName = title.stationName(),
                         departures = departingTrains,
                         arrivals = arrivingTrains,
                         stationInfo = stationInformation)
    }
}