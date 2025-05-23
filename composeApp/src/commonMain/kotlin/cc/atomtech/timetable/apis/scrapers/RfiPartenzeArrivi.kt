package cc.atomtech.timetable.apis.scrapers

import cc.atomtech.timetable.enumerations.StationOperator
import cc.atomtech.timetable.enumerations.TrainType
import cc.atomtech.timetable.models.rfi.StationBaseData
import cc.atomtech.timetable.models.rfi.TrainData
import cc.atomtech.timetable.models.rfi.TrainDelayStatus
import cc.atomtech.timetable.models.rfi.TrainStatus
import cc.atomtech.timetable.models.rfi.TrainStopData
import cc.atomtech.timetable.scrapers.RfiScraper

/**
 * @since 1.5.0
 */
object RfiPartenzeArrivi {
    suspend fun getSearchableEntries() : List<StationBaseData> {
        // TODO)) Move code here
        val stations = RfiScraper.getStations()
        return List(stations.size) { index ->
            StationBaseData(stations[index].id, stations[index].name, stationOperator = StationOperator.RFI_IT)
        }
    }

    suspend fun getDepartures(stationId: Int) : List<TrainData> {
        val state = RfiScraper.getStationTimetable(stationId)
        return List(state.departures.size) {
            val train = state.departures[it]
            TrainData(
                operator = train.operator,
                operatorName = "",
                category = train.category,
                categoryName = "",
                number = train.number,
                platform = train.platform,
                delay = TrainDelayStatus(
                    delay = train.delay,
                    status = TrainStatus.RUNNING
                ),
                station = train.station,
                time = train.time,
                stops = List(train.stops.size) { index ->
                    TrainStopData(
                        name = train.stops[index].name,
                        time = train.stops[index].time,
                        isCurrentStop = train.stops[index].isCurrentStop
                    )
                },
                details = train.details,
                trainType = TrainType.DEPARTURE
            )
        }
    }

    suspend fun getArrivals(stationId: Int) : List<TrainData> {
        val state = RfiScraper.getStationTimetable(stationId)
        return List(state.arrivals.size) {
            val train = state.arrivals[it]
            TrainData(
                operator = train.operator,
                operatorName = "",
                category = train.category,
                categoryName = "",
                number = train.number,
                platform = train.platform,
                delay = TrainDelayStatus(
                    delay = train.delay,
                    status = TrainStatus.RUNNING
                ),
                station = train.station,
                time = train.time,
                stops = List(train.stops.size) { index ->
                    TrainStopData(
                        name = train.stops[index].name,
                        time = train.stops[index].time,
                        isCurrentStop = train.stops[index].isCurrentStop
                    )
                },
                details = train.details,
                trainType = TrainType.ARRIVAL
            )
        }
    }
}
