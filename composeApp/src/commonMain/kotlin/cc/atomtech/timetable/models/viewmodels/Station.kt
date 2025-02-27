package cc.atomtech.timetable.models.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.atomtech.timetable.AppPreferences
import cc.atomtech.timetable.apis.scrapers.RfiPartenzeArrivi
import cc.atomtech.timetable.models.rfi.StationBaseData
import cc.atomtech.timetable.models.rfi.TrainData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class Station( private val preferences: AppPreferences ) : ViewModel() {
    private val _currentStation = mutableStateOf<StationBaseData?>(null)
    val currentStation: State<StationBaseData?> = _currentStation

    private val _info = mutableStateOf("")
    val info: State<String> = _info

    private val _departures = mutableStateOf<List<TrainData>>(emptyList())
    val departures: State<List<TrainData>> = _departures

    private val _arrivals = mutableStateOf<List<TrainData>>(emptyList())
    val arrivals: State<List<TrainData>> = _arrivals

    private val _loadingDepartures = mutableStateOf(false)
    val loadingDepartures: State<Boolean> = _loadingDepartures

    private val _loadingArrivals = mutableStateOf(false)
    val loadingArrivals: State<Boolean> = _loadingArrivals

    private val _allStationData = mutableStateOf<List<StationBaseData>>(emptyList())
    val allStationData: State<List<StationBaseData>> = _allStationData

    init {
        viewModelScope.launch {
            loadAllStations()
            getLastId()

            loadDepartures()
            loadArrivals()
        }
    }

    private fun List<StationBaseData>.findById(id: Int) : StationBaseData? {
        return this.firstOrNull { it.id == id }
    }

    fun List<StationBaseData>.searchByName(query: String) : List<StationBaseData> {
        if(query.isEmpty()) return emptyList()

        return _allStationData.value.filter { it.name.contains(query, ignoreCase = true) }
    }

    private fun updateById(id: Int) {
        _currentStation.value = _allStationData.value.findById(id)
    }

    private suspend fun getLastId() {
        val lastId = preferences.getStationId().first()
        updateById(lastId)
    }

    private suspend fun loadDepartures() {
        _loadingDepartures.value = true
        _departures.value = RfiPartenzeArrivi.getDepartures( stationId = _currentStation.value?.id ?: 1728 )
        _loadingDepartures.value = false
    }

    private suspend fun loadArrivals() {
        _loadingArrivals.value = true
        _arrivals.value = RfiPartenzeArrivi.getArrivals( stationId = _currentStation.value?.id ?: 1728 )
        _loadingArrivals.value = false
    }

    private suspend fun loadAllStations() {
        if(preferences.getStoreStations().first()) {
            val stationStore = preferences.getStationCache().first()
            _allStationData.value = Json.decodeFromString<List<StationBaseData>>(stationStore)
        } else {
            _allStationData.value = RfiPartenzeArrivi.getSearchableEntries()
        }
    }


    fun updateStationById(newId: Int) {
        updateById(newId)
        viewModelScope.launch {
            loadDepartures()
            loadArrivals()
        }
    }

    fun updateStation(stationData: StationBaseData) {
        _currentStation.value = stationData

        viewModelScope.launch {
            loadDepartures()
            loadArrivals()
        }
    }
}
