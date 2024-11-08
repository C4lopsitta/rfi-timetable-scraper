package cc.atomtech.rfi_timetable.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TimetableData(
    val stationName: String,
    val departures: List<TrainData>,
    val arrivals: List<TrainData>
)

class TimetableState(val stationName: String,
                     var departures: List<TrainData>,
                     var arrivals: List<TrainData>): ViewModel() {
    private val _uiState = MutableStateFlow(TimetableData(stationName, departures, arrivals))
    val uiState: StateFlow<TimetableData> = _uiState.asStateFlow()

    fun setNewTimetable(timeTable: TimetableData) {
        this._uiState.value = timeTable
    }

    fun getStationName(): String {
        return uiState.value.stationName
    }

    fun getDepartures(): List<TrainData> {
        return uiState.value.departures
    }

    fun getArrivals(): List<TrainData> {
        return uiState.value.arrivals
    }
}