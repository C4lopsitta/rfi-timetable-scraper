package cc.atomtech.timetable.models.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.atomtech.timetable.AppPreferences
import cc.atomtech.timetable.apis.scrapers.RfiPartenzeArrivi
import cc.atomtech.timetable.models.flows.UiEvent
import cc.atomtech.timetable.models.rfi.StationBaseData
import cc.atomtech.timetable.models.rfi.TrainData
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Station(
    private val preferences: AppPreferences,
    private val uiEvents: MutableSharedFlow<UiEvent>
) : ViewModel() {
    private val _currentStation = mutableStateOf<StationBaseData?>(null)
    val currentStation: State<StationBaseData?> = _currentStation

    private val _allStationData = mutableStateOf<List<StationBaseData>>(emptyList())
    val allStationData: State<List<StationBaseData>> = _allStationData

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

    private val _loadingStations = mutableStateOf(false)
    val loadingStations: State<Boolean> = _loadingStations

    private val IecHubJobSupervisor = SupervisorJob()
    private val IecHubCoroutineScope = CoroutineScope(Dispatchers.IO + IecHubJobSupervisor)

    // TODO)) Reimplement auto-reload

    init {
        IecHubCoroutineScope.launch {
            async { loadAllStations() }.await()
            getLastId()
        }

        loadDepartures()
        loadArrivals()
    }

    // region Private
    private fun List<StationBaseData>.findById(id: Int) : StationBaseData? {
        return this.firstOrNull { it.id == id }
    }

    private fun searchByName(query: String) : List<StationBaseData> {
        if(query.isEmpty()) return emptyList()
        return _allStationData.value.filter { it.name.contains(query, ignoreCase = true) }
    }

    private fun updateById(id: Int) {
        _currentStation.value = _allStationData.value.findById(id)
        println("[INFO] APP SUCKS DICKS!!!!!\n       BITCH COULDNT GET STATION OF ID $id!!!")

        CoroutineScope(Dispatchers.IO).launch {
            preferences.setStationId(id)
        }
    }

    private suspend fun getLastId() {
        val lastId = preferences.getStationId().first()
        updateById(lastId)
    }

    private fun loadDepartures() {
        viewModelScope.launch {
            _loadingDepartures.value = true
            try {
                _departures.value =
                    RfiPartenzeArrivi.getDepartures(stationId = _currentStation.value?.id ?: 1728)
            } catch (ex: Exception) {
                uiEvents.emit(UiEvent.RfiTimetableScrapingException(ex))
            }
            _loadingDepartures.value = false
        }
    }

    private fun loadArrivals() {
        viewModelScope.launch {
            _loadingArrivals.value = true
            try {
                _arrivals.value =
                    RfiPartenzeArrivi.getArrivals(stationId = _currentStation.value?.id ?: 1728)
            } catch (ex: Exception) {
                uiEvents.emit(UiEvent.RfiTimetableScrapingException(ex))
            }
            _loadingArrivals.value = false
        }
    }

    private suspend fun loadAllStations(): Unit {
        if(preferences.getStoreStations().first()) {
            val stationStore = preferences.getStationCache().first()
            if(stationStore.isNotEmpty()) {
                if(stationStore[0] == '[') {
                    _allStationData.value =
                        Json.decodeFromString<List<StationBaseData>>(stationStore)
                } else {
                    preferences.setStationCache("")
                    loadAllStations()
                }
            } else {
                _allStationData.value = RfiPartenzeArrivi.getSearchableEntries()
                preferences.setStationCache(Json.encodeToString<List<StationBaseData>>(_allStationData.value))
            }
        } else {
            _allStationData.value = RfiPartenzeArrivi.getSearchableEntries()
            val bookmarkedJson = preferences.getStationCache().first()
            if(bookmarkedJson.isNotEmpty()) {
                val bookmarked = Json.decodeFromString<List<StationBaseData>>(bookmarkedJson)

                bookmarked.forEach { bookmark ->
                    _allStationData.value.findById(bookmark.id)?.let {
                        it.isBookmarked = true
                    }
                }
            }
        }
    }
    // endregion Private

    suspend fun updateBookmarkedStations() {
        val data = if(preferences.getStoreStations().first()) {
            _allStationData.value
        } else {
            _allStationData.value.filter { it.isBookmarked }
        }
        preferences.setStationCache(Json.encodeToString<List<StationBaseData>>(data))
    }


    fun searchStations(query: String) : List<StationBaseData> {
        return searchByName(query)
    }

    fun update() {
        viewModelScope.cancel(CancellationException("New loading action launched"))
        loadDepartures()
        loadArrivals()
    }

    fun updateStationById(newId: Int) {
        updateById(newId)
        try {
            viewModelScope.cancel(CancellationException("New loading action launched"))
        } catch (_: Exception) {}

        loadDepartures()
        loadArrivals()
    }

    fun updateStation(stationData: StationBaseData) {
        _currentStation.value = stationData

        viewModelScope.cancel(CancellationException("New loading action launched"))
        loadDepartures()
        loadArrivals()
    }
}
