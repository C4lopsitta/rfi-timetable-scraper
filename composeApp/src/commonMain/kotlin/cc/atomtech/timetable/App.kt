package cc.atomtech.timetable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.*
import androidx.navigation.compose.currentBackStackEntryAsState
import cc.atomtech.timetable.components.NavBar
import cc.atomtech.timetable.components.NavRail
import cc.atomtech.timetable.components.NavigationBodyHost
import cc.atomtech.timetable.components.ScaffoldBody
import cc.atomtech.timetable.models.Station
import cc.atomtech.timetable.models.Stations
import cc.atomtech.timetable.models.TimetableState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okio.Path.Companion.toPath
import org.jetbrains.compose.ui.tooling.preview.Preview
import cc.atomtech.timetable.components.AppBar
import cc.atomtech.timetable.components.InfoLavoriTabBar
import cc.atomtech.timetable.scrapers.RfiScraper
import cc.atomtech.timetable.scrapers.RssFeeds
import cc.atomtech.timetable.views.DeviceOffline


const val preferencesFile = "timetables-prefs.preferences_pb"

@Composable expect fun storePreferences(): DataStore<Preferences>

@Composable expect fun isNetworkAvailable(): Boolean


fun instantiatePreferences(createPath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { createPath().toPath() }
    )

@Composable
@Preview
fun Main(navController: NavHostController,
         isDesktop: Boolean = false,
         preferences: AppPreferences,
         colorScheme: ColorScheme? = null) {
    var stationId by remember { mutableStateOf(1728) }
    var favouriteStations by remember { mutableStateOf<Stations>(Stations(arrayListOf())) }

    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    var searchSuggestions by remember { mutableStateOf<List<Station>?>(null) }
    val stations by remember { mutableStateOf(Stations(arrayListOf())) }
    var reloadTrigger by remember { mutableStateOf(false) }
    var timetable by remember { mutableStateOf<TimetableState?>(null) }

    var reloaderMinutes by remember { mutableStateOf(5) }
    var isNewStationSet by remember { mutableStateOf(true) }
    var timetableRefresher: Job? = null

    var tabIndex by remember { mutableStateOf(0) }

    val snackbarHostState = SnackbarHostState()

    LaunchedEffect(Unit) {
        try {
            stationId = preferences.getStationId().first()
            stations.stations = RfiScraper.getStations()
            favouriteStations = Stations.fromFavourites(preferences.getFavouriteStations().first(), stations)
        } catch (e: Exception) {
            println(e.printStackTrace())
            error = e.toString()
        }
    }

    LaunchedEffect(stationId, reloadTrigger) {
        try {
            loading = true
            if(isNewStationSet) {
                timetable = null
                timetable = RfiScraper.getStationTimetable(stationId)
            } else {
                timetable?.setNewTimetable(RfiScraper.reloadStation(stationId))
            }
            loading = false
            isNewStationSet = false

            val feed = RssFeeds.fetchRss(RssFeeds.allRegionsLive)

        } catch (_: CancellationException) {
        } catch (e: Exception) {
            println(e.printStackTrace())
            error = e.toString()

            e.message?.let {
                val snackBarResults = snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Long,
                    actionLabel = "Retry"
                ).run {
                    when (this) {
                        SnackbarResult.Dismissed -> return@let
                        SnackbarResult.ActionPerformed -> { reloadTrigger = !reloadTrigger }
                    }
                }
            }
        } finally {
            timetableRefresher?.cancel()
            // stores how many 5 minutes it has to wait, so multiply by 5 to get actual value, if 0 skip reload
            reloaderMinutes = preferences.getReloadDelay().first() * 5
            if(reloaderMinutes > 0) {
                timetableRefresher = CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
                    delay((reloaderMinutes * 60 * 1000).toLong())
                    reloadTrigger = !reloadTrigger
                }
            }
        }
    }

    val actualColorScheme = if(colorScheme == null) {
        if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    } else colorScheme


    MaterialTheme (
        colorScheme = actualColorScheme
    ) {
        Scaffold (
            topBar = {
                if(!isDesktop) {
                    if(navController.currentBackStackEntryAsState().value?.destination?.route
                        ?.contains("infolavori") == true ) {
                        InfoLavoriTabBar(tabIndex) { tabIndex = it }
                    } else {
                        AppBar(
                            navController = navController,
                            searchQuery = searchQuery,
                            timetable = timetable,
                            triggerReload = { reloadTrigger = !reloadTrigger },
                            updateSearchQuery = { query: String ->
                                searchQuery = query
                                if (stations.stations.isNotEmpty())
                                    searchSuggestions = stations.search(query)
                            },
                            resetSearchSuggestions = {
                                navController.popBackStack()
                                searchQuery = ""
                                searchSuggestions = listOf()
                            },
                        )
                    }
                }
            },
            snackbarHost = {
                SnackbarHost( snackbarHostState )
            },
            bottomBar = { if(!isDesktop) NavBar(navController) },
            floatingActionButton = {

            }
        ) { paddingValues ->
            ScaffoldBody(isLoading = loading,
                isDesktop = isDesktop,
                paddingValues = paddingValues,
                navRail = {
                    if (isDesktop) {
                        NavRail(navController = navController) { reloadTrigger = !reloadTrigger }
                    }
                }) {
//                if(isNetworkAvailable()) {
//                    DeviceOffline {
//                        reloadTrigger = !reloadTrigger
//                    }
//                } else {
                    NavigationBodyHost(
                        navController = navController,
                        isDesktop = isDesktop,
                        isLoading = loading,
                        timetable = timetable,
                        stations = stations,
                        tabIndex = tabIndex,
                        favouriteStations = favouriteStations,
                        searchSuggestions = searchSuggestions,
                        preferences = preferences,
                        setStationId = { newId ->
                            stationId = newId
                            isNewStationSet = true
                            CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
                                preferences.setStationId(newId)
                                navController.navigate("departures")
                            }
                        },
                        updateFavourites = { favourites: String ->
                            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                                preferences.setFavouriteStations(favourites)
                            }
                        }
                    )
                }
//            }
        }
    }
}

