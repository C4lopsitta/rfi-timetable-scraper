package cc.atomtech.timetable.apis.scrapers

import cc.atomtech.timetable.enumerations.StationOperator
import cc.atomtech.timetable.enumerations.TrainType
import cc.atomtech.timetable.models.rfi.StationBaseData
import cc.atomtech.timetable.models.rfi.TrainData
import cc.atomtech.timetable.models.rfi.TrainDelayStatus
import cc.atomtech.timetable.models.rfi.TrainStatus
import cc.atomtech.timetable.models.rfi.TrainStopData
import cc.atomtech.timetable.scrapers.RfiScraper

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
        // TODO)) Move code here
        return RfiScraper.getStations()
    }

    suspend fun getDepartures(stationId: Int) : List<TrainData> {
        return RfiScraper.getStationTimetable(stationId).departures
    }

    suspend fun getArrivals(stationId: Int) : List<TrainData> {
        return RfiScraper.getStationTimetable(stationId).arrivals
    }
}
