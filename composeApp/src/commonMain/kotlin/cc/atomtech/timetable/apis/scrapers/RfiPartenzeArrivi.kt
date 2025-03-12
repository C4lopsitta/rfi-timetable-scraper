package cc.atomtech.timetable.apis.scrapers

import cc.atomtech.timetable.enumerations.StationCountry
import cc.atomtech.timetable.models.rfi.StationBaseData
import cc.atomtech.timetable.models.rfi.TrainData
import cc.atomtech.timetable.scrapers.RfiScraper

object RfiPartenzeArrivi {
    suspend fun getSearchableEntries() : List<StationBaseData> {
        // TODO)) Move code here
        val stations = RfiScraper.getStations()
        return List(stations.size) { index ->
            StationBaseData(stations[index].id, stations[index].name, stationCountry = StationCountry.RFI_IT)
        }
    }

    suspend fun getDepartures(stationId: Int) : List<TrainData> {
        TODO()
    }

    suspend fun getArrivals(stationId: Int) : List<TrainData> {
        TODO()
    }
}
