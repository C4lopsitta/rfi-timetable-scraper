package cc.atomtech.timetable.models.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.atomtech.timetable.AppPreferences
import cc.atomtech.timetable.models.flows.UiEvent
import cc.atomtech.timetable.models.utilities.MatchedStation
import cc.atomtech.timetable.models.rfi.StationBaseData
import cc.atomtech.timetable.apis.scrapers.RfiPartenzeArrivi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import cc.atomtech.timetables.resources.Res
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi

/** Handles the loading of all stations (as a list of ID-Name).
 *
 * @see MatchedStation
 * @see StationBaseData
 * @see cc.atomtech.timetable.apis.scrapers.RfiPartenzeArrivi.getSearchableEntries
 */
class StationListViewModel(
    private val uiEvents: MutableSharedFlow<UiEvent>,
    private val appPreferences: AppPreferences
): ViewModel() {
    private val MATCHED_STATIONS_FILE = "matchedStations.json"
    private val json = Json { ignoreUnknownKeys = true }

    private val _stationsFromFile = MutableStateFlow<List<MatchedStation>>(emptyList())
    private val _rfiStations = MutableStateFlow<List<StationBaseData>>(emptyList())

    val fileStations: StateFlow<List<MatchedStation>> = _stationsFromFile.asStateFlow()

    init {
        viewModelScope.launch {
            loadFile()
            loadRfiStations()
        }
    }


    /**
     * Loads the matched stations from the file.
     *
     * @see [Res]
     */
    @OptIn(ExperimentalResourceApi::class)
    private suspend fun loadFile() {
        val jsonContent = Res.readBytes("files/$MATCHED_STATIONS_FILE").decodeToString()

        val stations = json.decodeFromString<List<MatchedStation>>(jsonContent)
        _stationsFromFile.value = stations
    }

    /**
     * Loads stations from the Cache or, when unavailable, from the Scraper.
     *
     * @see [AppPreferences]
     * @see [RfiPartenzeArrivi.getSearchableEntries]
     */
    private suspend fun loadRfiStations() {
        var stations = emptyList<StationBaseData>()
        appPreferences.getStationCache().collect {
            if(it.isNotEmpty()) {
                try {
                    stations = json.decodeFromString<List<StationBaseData>>(it)
                    _rfiStations.value = stations
                } catch (_: Exception) {}
            }

            stations.ifEmpty {
                try {
                    stations = RfiPartenzeArrivi.getSearchableEntries()
                    _rfiStations.value = stations
                    appPreferences.setStationCache(json.encodeToString(stations))
                } catch (ex: Exception) {
                    uiEvents.emit(UiEvent.StationLoadingException(ex))
                }
            }

            _rfiStations.value = stations
        }
    }
}