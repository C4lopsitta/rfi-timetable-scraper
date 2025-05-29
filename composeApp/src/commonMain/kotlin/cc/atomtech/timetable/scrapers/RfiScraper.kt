package cc.atomtech.timetable.scrapers

import cc.atomtech.timetable.enumerations.Category
import cc.atomtech.timetable.enumerations.Operator
import cc.atomtech.timetable.enumerations.StationOperator
import cc.atomtech.timetable.enumerations.TrainType
import cc.atomtech.timetable.models.TimetableData
import cc.atomtech.timetable.models.TimetableState
import cc.atomtech.timetable.models.rfi.StationBaseData
import cc.atomtech.timetable.models.rfi.TrainData
import cc.atomtech.timetable.models.rfi.TrainDelayStatus
import cc.atomtech.timetable.models.rfi.TrainStatus
import cc.atomtech.timetable.models.rfi.TrainStopData
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.select.Elements


@Deprecated("Deprecated since v.1.5.0", ReplaceWith("cc.atomtech.apis.scrapers.RfiPartenzeArrivi"), DeprecationLevel.ERROR)
object RfiScraper {
    private fun tableToTrainList(tableRows: Elements?, isDepartures: Boolean): List<TrainData> {
        val trains: ArrayList<TrainData> = arrayListOf()
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

                    val stops = arrayListOf<TrainStopData>()
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
                                    TrainStopData(
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
                        TrainData(
                            number = trainNumber,
                            operator = Operator.fromString(operator),
                            operatorName = operator,
                            category = Category.fromString(category, operator),
                            platform = platform,
                            station = station.stationName(),
                            time = time,
                            delay = TrainDelayStatus(
                                delay = delayMinutes,
                                status = when(delay) {
                                    "RITARDO" -> TrainStatus.DELAYED
                                    "Cancellato" -> TrainStatus.CANCELLED
                                    else -> TrainStatus.RUNNING
                                }
                            ),
                            details = moreInformationString,
                            stops = stops,
                            trainType = if(isDepartures) TrainType.DEPARTURE else TrainType.ARRIVAL
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

        val departingTrains: List<TrainData> = tableToTrainList(departuresTableBody?.getElementsByTag("tr"), true)
        val arrivingTrains: List<TrainData> = tableToTrainList(arrivalsTableBody?.getElementsByTag("tr"), false)

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