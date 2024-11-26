package cc.atomtech.timetable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Announcement
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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


const val preferencesFile = "timetables-prefs.preferences_pb"

@Composable
expect fun storePreferences(): DataStore<Preferences>


fun instantiatePreferences(createPath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { createPath().toPath() }
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun Main(navController: NavHostController,
         isDesktop: Boolean = false) {
    val preferences = AppPreferences(storePreferences())

    var stationId by remember { mutableStateOf(1728) }
    var favouriteStations by remember { mutableStateOf<Stations>(Stations(arrayListOf())) }

    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    var searchSuggestions by remember { mutableStateOf<List<Station>?>(null) }
    val stations by remember { mutableStateOf(Stations(arrayListOf())) }
    var reloadTrigger by remember { mutableStateOf(false) }
    var timetable by remember { mutableStateOf<TimetableState?>(null) }

    val reloaderMinutes = 5
    var isNewStationSet by remember { mutableStateOf(true) }
    var timetableRefresher: Job? = null

    var tabIndex by remember { mutableStateOf(0) }

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
                println("Full reload")
                timetable = null
                timetable = RfiScraper.getStationTimetable(stationId)
            } else {
                timetable?.setNewTimetable(RfiScraper.reloadStation(stationId))
            }
            loading = false
            isNewStationSet = false

            val feed = RssFeeds.fetchRss(RssFeeds.allRegionsLive)
            for (item in RssFeeds.parseFeed(feed)) {
                println(item)
            }

        } catch (_: CancellationException) {
        } catch (e: Exception) {
            println(e.printStackTrace())
            error = e.toString()
        } finally {
            timetableRefresher?.cancel()
            if(reloaderMinutes > 0) {
                timetableRefresher = CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
                    delay((reloaderMinutes * 60 * 1000).toLong())
                    reloadTrigger = !reloadTrigger
                }
            }
        }
    }

    val colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    if(error != null) {
        BasicAlertDialog(
            onDismissRequest = {
                error = null
            },
        ) {
            Card () {
                Column (
                    modifier = Modifier.padding( 16.dp )
                ) {
                    Text(Strings.get("error_data_fetch"), fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Text(error.toString())
                    ElevatedButton(
                        content = { Text("Ok") },
                        onClick = {
                            error = null
                        }
                    )
                }
            }
        }
    }
    MaterialTheme (
        colorScheme = colorScheme
    ) {
        Scaffold (
            topBar = {
                if(!isDesktop) {
                    if(navController.currentBackStackEntryAsState().value?.destination?.route
                        ?.contains("infolavori") == true ) {
                        TabRow (
                            selectedTabIndex = tabIndex
                        ) {
                            Tab(
                                selected = true,
                                onClick = { tabIndex = 0 },
                                text = { Text(Strings.get("real_time")) },
                                icon = { Icon(Icons.Rounded.Timelapse, contentDescription = Strings.get("real_time")) }
                            )
                            Tab(
                                selected = true,
                                onClick = { tabIndex = 1 },
                                text = { Text(Strings.get("announcements")) },
                                icon = { Icon(Icons.AutoMirrored.Rounded.Announcement, contentDescription = Strings.get("announcements")) }
                            )
                            Tab(
                                selected = true,
                                onClick = { tabIndex = 2 },
                                text = { Text(Strings.get("trenitalia")) },
                                icon = { Icon(Icons.Rounded.Train, contentDescription = Strings.get("trenitalia")) }
                            )
                        }
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
            bottomBar = { if(!isDesktop) NavBar(navController) }
        ) { paddingValues ->
            ScaffoldBody(isLoading = loading,
                isDesktop = isDesktop,
                paddingValues = paddingValues,
                navRail = {
                    if (isDesktop) {
                        NavRail(navController = navController) { reloadTrigger = !reloadTrigger }
                    }
                }) {
                NavigationBodyHost(
                    navController = navController,
                    isDesktop = isDesktop,
                    isLoading = loading,
                    timetable = timetable,
                    stations = stations,
                    tabIndex = tabIndex,
                    favouriteStations = favouriteStations,
                    searchSuggestions = searchSuggestions,
                    setStationId = { newId ->
                        stationId = newId
                        navController.popBackStack()
                        isNewStationSet = true
                        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                            preferences.setStationId(newId)
                        }
                    },
                    updateFavourites = { favourites: String ->
                        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                            preferences.setFavouriteStations(favourites)
                        }
                    }
                )
            }
        }
    }
}

