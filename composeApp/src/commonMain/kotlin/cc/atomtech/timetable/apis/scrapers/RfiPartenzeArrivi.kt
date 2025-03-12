package cc.atomtech.timetable.apis.scrapers

import cc.atomtech.timetable.models.rfi.StationBaseData
import cc.atomtech.timetable.models.rfi.TrainData

object RfiPartenzeArrivi {
    suspend fun getSearchableEntries() : List<StationBaseData> {

    }

    suspend fun getDepartures(stationId: Int) : List<TrainData> {

    }

    suspend fun getArrivals(stationId: Int) : List<TrainData> {

    }
}
