package cc.atomtech.timetable.apis.scrapers

import cc.atomtech.timetable.enumerations.Category
import cc.atomtech.timetable.enumerations.Operator
import cc.atomtech.timetable.enumerations.StationOperator
import cc.atomtech.timetable.enumerations.TrainType
import cc.atomtech.timetable.extenions.toStationName
import cc.atomtech.timetable.models.rfi.StationBaseData
import cc.atomtech.timetable.models.rfi.TrainData
import cc.atomtech.timetable.models.rfi.TrainDelayStatus
import cc.atomtech.timetable.models.rfi.TrainStatus
import cc.atomtech.timetable.models.rfi.TrainStopData
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element

private object IecHubHtmlTagsIds {
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
    const val STATIONS_LIST = "ElencoLocalita"
}

private object IecHubHtmlTagsClasses {
    const val DETAILS_POPUP_ELEMENT = "FermateSuccessivePopupSytle"
}

private object IecHubUrls {
    private val baseUrl = "https://iechub.rfi.it/ArriviPartenze/ArrivalsDepartures/"
    val stationsUrl = "${baseUrl}Home"
    val monitorUrl = "${baseUrl}Monitor"
    const val baseQueryDepartures = "?Arrivals=False&PlaceId="
    const val baseQueryArrivals = "?Arrivals=True&PlaceId="
}


/** Scraper for RFI's `IecHub` webpages.
 *
 *  Handles scraping of various pages:
 *  - `iframe` for the station selecting
 *  - station page with both Arrivals and Departures
 *
 * @author Simone Robaldo
 * @see Ksoup
 * @since 1.5.0
 */
object RfiPartenzeArrivi {
    // region Ksoup Extensions

    fun Document.getTimetable(): Element? = this.body().getElementById(IecHubHtmlTagsIds.TABLE)

    fun Element.getHtmlOfFirstChildrenById(id: String) = this.getElementById(id)
        ?.children()?.first()?.html()

    // endregion Ksoup Extensions

    /** Fetches and returns a list of all stations as a list of [StationBaseData].
     *
     *  @see StationBaseData
     *  @since 1.5.0
     *  @author Simone Robaldo
     */
    suspend fun getSearchableEntries() : List<StationBaseData> {
        val stations = Ksoup.parseGetRequest(url = IecHubUrls.stationsUrl)
        val stationList = stations.body().getElementById(IecHubHtmlTagsIds.STATIONS_LIST)

        val stationsList: ArrayList<StationBaseData> = arrayListOf()

        stationList?.getElementsByTag("option")?.forEach { option ->
            stationsList.add(
                StationBaseData(
                    name = option.html().toStationName(),
                    id = option.value().toInt(),
                    stationOperator = StationOperator.RFI_IT
                )
            )
        }

        return stationsList
    }

    // TODO)) Find way to fetch `stationInfo` without separate scrape

    suspend fun getDepartures(stationId: Int) : List<TrainData> {
        val departures = Ksoup.parseGetRequest(url="${IecHubUrls.monitorUrl}${IecHubUrls.baseQueryDepartures}${stationId}")

        return timetableToTrainList(departures.getTimetable())
    }

    suspend fun getArrivals(stationId: Int) : List<TrainData> {
        val arrivals = Ksoup.parseGetRequest(url="${IecHubUrls.monitorUrl}${IecHubUrls.baseQueryArrivals}${stationId}")

        return timetableToTrainList(arrivals)
    }

    // region Private Methods

    // TODO)) Create custom excepiton and add in error
    /** Given an [Element], if not null, it will create a list of [TrainData]
     *
     * Parses the table taken from the parsed HTML with the method [getTimetable] and creates individual
     * trains when available.
     *
     * @throws TODO
     * @since 1.5.0
     * @see [rowToTrainData]
     * @author Simone Robaldo
     *
     */
    private fun timetableToTrainList(timetable: Element?): List<TrainData> {
        if(timetable == null) throw Exception("")

        val tableRows = timetable.getElementsByTag("tr")
        val trains = arrayListOf<TrainData>()

        tableRows.forEach { trainRow ->
            rowToTrainData(trainRow)?.let {
                trains.add(it)
            }
        }

        return trains
    }

    /** Parses an individual table row taken from the timetable HTML table.
     *
     * @see [TrainData]
     * @see [TrainStopData]
     * @see [getStopsFromDetails]
     * @since 1.5.0
     * @author Simone Robaldo
     */
    private fun rowToTrainData(row: Element): TrainData? {
        // Check if the row is a header, and skip it if it is,
        // otherwise check if it has an id and skip it if it does not
        if(row.children().first()?.tagName() == "th" || row.id().isEmpty()) return null


        val trainNumber = row.id()

        // Fetch the `alt` attribute of the image of the Operator. Set it to "UNDEFINED" if couldn't fetch it
        val operator = try {
            row.getElementById(IecHubHtmlTagsIds.OPERATOR_FIELD)
                ?.children()?.first()?.attribute("alt")?.value ?: "UNDEFINED"
        } catch (_: Exception) { "UNDEFINED" }

        // Fetch the `alt` attribute of the image of the Category. Set it to "UNDEFINED" if couldn't fetch it
        val category = try {
            row.getElementById(IecHubHtmlTagsIds.CATEGORY_FIELD)
                ?.children()?.first()?.attribute("alt")?.value ?: "UNDEFINED"
        } catch (_: Exception) { "UNDEFINED" }

        val platform = row.getHtmlOfFirstChildrenById(IecHubHtmlTagsIds.PLATFORM_FIELD) ?: "UNDEFINED"
        val station = row.getHtmlOfFirstChildrenById(IecHubHtmlTagsIds.STATION_FIELD) ?: "UNDEFINED"
        val time = row.getHtmlOfFirstChildrenById(IecHubHtmlTagsIds.TIME_FIELD) ?: "UNDEFINED"
        val delay = row.getHtmlOfFirstChildrenById(IecHubHtmlTagsIds.DELAY_FIELD) ?: "UNDEFINED"

        var trainDetails = ""
        var nextStationsString = ""

        if(row.getElementById(IecHubHtmlTagsIds.DETAILS_BUTTON_FIELD)?.children()?.size != 0) {
            val detailsPopupElements =
                row.getElementsByClass(IecHubHtmlTagsClasses.DETAILS_POPUP_ELEMENT)
                    .first()?.children() ?: emptyList()

            for(i in detailsPopupElements.indices step 1) {
                if(detailsPopupElements[i].html() == "Fermate successive")
                    nextStationsString = detailsPopupElements[i + 1].html()
                if(detailsPopupElements[i].html() == "Informazioni")
                    trainDetails = detailsPopupElements[i+1].html()
            }
        }

        return TrainData(
            operator = Operator.fromString(operator),
            operatorName = operator,
            category = Category.fromString(category, operator),
            categoryName = category,
            number = trainNumber,
            platform = platform,
            delay = getDelay(delay),
            station = station,
            time = time,
            stops = getStopsFromDetails(nextStationsString),
            details = trainDetails,
            trainType = TrainType.ARRIVAL
        )
    }

    private fun getStopsFromDetails(stationsString: String): List<TrainStopData> {
        val stops = arrayListOf<TrainStopData>()

        if(stationsString.isNotEmpty()) {
            stationsString.split("FERMA A: ")[1]
            stationsString.split(" - ").forEach { stop ->
                var stopTime = stop.split("(")[1]
                    .removeSuffix(")")
                    .replace(".", ":")
                if(stopTime.split(":").size == 1) stopTime = "0$stopTime"
                stops.add(
                    TrainStopData(
                        name = stop.split(" (")[0].removePrefix("FERMA A: ").toStationName(),
                        time = stopTime,
                        isCurrentStop = false
                    )
                )
            }
        }

        return stops.toList()
    }

    private fun getDelay(delay: String): TrainDelayStatus {
        return TrainDelayStatus(
            delay = delay.toInt(),
            status = when(delay) {
                "RITARDO" -> TrainStatus.DELAYED
                "Cancellato" -> TrainStatus.CANCELLED
                else -> if(delay.toInt() > 0) TrainStatus.DELAYED else TrainStatus.RUNNING
            }
        )
    }

    // endregion Private Methods
}
