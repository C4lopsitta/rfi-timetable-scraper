package cc.atomtech.timetable.apis.scrapers

import cc.atomtech.timetable.enumerations.StationOperator
import cc.atomtech.timetable.extenions.toStationName
import cc.atomtech.timetable.models.rfi.StationBaseData
import cc.atomtech.timetable.models.rfi.TrainData
import cc.atomtech.timetable.scrapers.RfiScraper
import cc.atomtech.timetable.scrapers.RfiScraper.stationName
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest

private object HtmlTagsIdNames {
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
 * @since 1.5.0
 */
object RfiPartenzeArrivi {

    /** Fetches and returns a list of all stations as a list of [StationBaseData].
     *
     *  @see StationBaseData
     *  @since 1.5.0
     */
    suspend fun getSearchableEntries() : List<StationBaseData> {
        val stations = Ksoup.parseGetRequest(url = IecHubUrls.stationsUrl)
        val stationList = stations.body().getElementById(HtmlTagsIdNames.STATIONS_LIST)

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

    suspend fun getDepartures(stationId: Int) : List<TrainData> {
        return RfiScraper.getStationTimetable(stationId).departures
    }

    suspend fun getArrivals(stationId: Int) : List<TrainData> {
        return RfiScraper.getStationTimetable(stationId).arrivals
    }
}
