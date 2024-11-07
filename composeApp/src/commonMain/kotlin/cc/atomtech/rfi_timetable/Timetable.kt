package cc.atomtech.rfi_timetable

class Timetable {
    private var stationId: Int?
    private var stationName: String? = null
    private lateinit var departures: List<TrainData>
    private lateinit var arrivals: List<TrainData>

    constructor(stationId: Int) {
        this.stationId = stationId
    }

    suspend fun getDeparturesTimetableData(): String {
        return ""
    }
}