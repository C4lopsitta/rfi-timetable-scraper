package cc.atomtech.rfi_timetable

import cc.atomtech.rfi_timetable.models.TimetableData
import cc.atomtech.rfi_timetable.models.TimetableState
import cc.atomtech.rfi_timetable.models.TrainData
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import java.util.Locale
import kotlin.io.encoding.ExperimentalEncodingApi

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
    const val DETAILS_FILED_BUTTON = "RDettagli"
}

object RfiScraper {
    private const val baseUrl = "https://iechub.rfi.it/ArriviPartenze/ArrivalsDepartures/Monitor"
    private const val baseQueryDepartures = "?Arrivals=False&PlaceId="
    private const val baseQueryArrivals = "?Arrivals=True&PlaceId="

    suspend fun getSuggestedStations(query: String) {

    }

    private fun String.stationName(): String {
        this.lowercase(Locale.getDefault())
        return this.lowercase().split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercaseChar() }
        }
    }


    @OptIn(ExperimentalEncodingApi::class)
    suspend fun getStationTimetable(stationId: Int): TimetableState {
        val departures = Ksoup.parseGetRequest(url="${baseUrl}${baseQueryDepartures}${stationId}")

        val title = departures.body().getElementById(HtmlTagsIdNames.STATION_NAME)?.html() ?: "Error"
        val departuresTableBody = departures.body().getElementById(HtmlTagsIdNames.TABLE)
        val stationInfo = departures.body().getElementById(HtmlTagsIdNames.STATION_INFO)

        val departingTrains: ArrayList<TrainData> = arrayListOf()
        val arrivingTrains: ArrayList<TrainData> = arrayListOf()

        departuresTableBody?.getElementsByTag("tr")?.forEach { tr ->
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

                    var delayMinutes = 0
                    if (delay == "RITARDO") {
                        delayMinutes = Int.MAX_VALUE
                    } else if (delay == "Cancellato") {
                        delayMinutes = Int.MIN_VALUE
                    } else if (delay.isNotEmpty()) {
                        delayMinutes = delay.toInt()
                    }

                    departingTrains.add(
                        TrainData(
                            number = trainNumber,
                            operatorName = operator,
                            category = category,
                            platform = platform,
                            station = station.stationName(),
                            time = time,
                            delay = delayMinutes
                        )
                    )
                }
            }
        }

//        println("Recieved data:\nSTATION $title\n${tableBody?.html()}\n${stationInfo?.html()}")

        return TimetableState(stationName = title.stationName(),
                         departures = departingTrains.toList(),
                         arrivals = arrivingTrains.toList())
    }
}