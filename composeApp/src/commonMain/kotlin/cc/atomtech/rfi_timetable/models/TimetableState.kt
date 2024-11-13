package cc.atomtech.rfi_timetable.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TimetableData(
    val stationName: String,
    val departures: List<TrainData>,
    val arrivals: List<TrainData>,
    val stationInfo: String?
)

class TimetableState(val stationName: String,
                     var departures: List<TrainData>,
                     var arrivals: List<TrainData>,
                     val stationInfo: String? = null): ViewModel() {
    private val _uiState = MutableStateFlow(TimetableData(stationName, departures, arrivals, stationInfo))
    val uiState: StateFlow<TimetableData> = _uiState.asStateFlow()

    fun setNewTimetable(timeTable: TimetableData) {
        this._uiState.value = timeTable
    }
}