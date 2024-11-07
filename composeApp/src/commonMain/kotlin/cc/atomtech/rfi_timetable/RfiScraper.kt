package cc.atomtech.rfi_timetable

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Document

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

    suspend fun getStationTimetable(stationId: Int): List<TrainData> {
        val departures = Ksoup.parseGetRequest(url="${baseUrl}${baseQueryDepartures}${stationId}")

        val title = departures.body().getElementById(HtmlTagsIdNames.STATION_NAME)?.html() ?: "Error"
        val tableBody = departures.body().getElementById(HtmlTagsIdNames.TABLE)
            ?: throw Exception("No station data")
        val stationInfo = departures.body().getElementById(HtmlTagsIdNames.STATION_INFO)

        val trains: ArrayList<TrainData> = arrayListOf()

        tableBody.getElementsByTag("tr").forEach { tr ->
            if(tr.children()[0].tagName() != "th") {
                if(tr.id().isNotEmpty()) {
                    println(tr.id())

                    val tmpTrain = tr.id()
                    var trainNumber: Int
                    try {
                        trainNumber = tmpTrain.toInt()
                    } catch (e: Exception) {
                        trainNumber = 0
                    }

                    val operator =
                        tr.getElementById(HtmlTagsIdNames.OPERATOR_FIELD)!!.children()[0].attribute(
                            "alt"
                        )?.value ?: "UNDEFINED"
                    val category =
                        tr.getElementById(HtmlTagsIdNames.CATEGORY_FIELD)!!.children()[0].attribute(
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

                    trains.add(
                        TrainData(
                            number = trainNumber,
                            operatorName = operator,
                            category = category,
                            platform = platform.toInt(),
                            station = station,
                            time = time,
                            delay = if(delay.isNotEmpty()) delay.toInt() else 0
                        )
                    )
                }
            }

        }

//        println("Recieved data:\nSTATION $title\n${tableBody?.html()}\n${stationInfo?.html()}")

        return trains.toList()
    }
}