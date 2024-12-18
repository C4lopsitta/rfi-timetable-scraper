package cc.atomtech.timetable.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock

data class TimetableData(
    val stationName: String,
    val departures: List<TrainData>,
    val arrivals: List<TrainData>,
    val stationInfo: String?,
    var lastUptade: Long = Clock.System.now().toEpochMilliseconds()
)

class TimetableState(val stationName: String,
                     var departures: List<TrainData>,
                     var arrivals: List<TrainData>,
                     val stationInfo: String? = null): ViewModel() {
    private val _uiState = MutableStateFlow(TimetableData(stationName, departures, arrivals, stationInfo))
    val uiState: StateFlow<TimetableData> = _uiState.asStateFlow()

    fun setNewTimetable(timeTable: TimetableData) {
        this._uiState.value = timeTable
        this._uiState.value.lastUptade = Clock.System.now().toEpochMilliseconds()
    }
}