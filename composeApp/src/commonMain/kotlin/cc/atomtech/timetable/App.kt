package cc.atomtech.timetable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.*
import androidx.navigation.compose.*
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okio.Path.Companion.toPath
import org.jetbrains.compose.ui.tooling.preview.Preview
import rfi.composeapp.generated.resources.Res
import java.time.Duration
import java.time.temporal.TemporalAmount


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
                    Text("Data fetch error", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
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
            topBar = { if(!isDesktop) TopAppBar(
                    title = {
                        if(navController.currentBackStackEntryAsState().value?.destination?.route?.contains("search") == false) {
                            Text(timetable?.stationName ?: "Timetables")
                        } else {
                            TextField(value = searchQuery,
                                onValueChange = { query: String ->
                                    searchQuery = query
                                    if(stations.stations.isNotEmpty())
                                        searchSuggestions = stations.search(query)
                                },
                                placeholder = { Text("Search") },
                                maxLines = 1,
                                modifier = Modifier.fillMaxWidth().padding( horizontal = 12.dp ),
                                shape = TextFieldDefaults.OutlinedTextFieldShape,
                            )
                        }
                    },
                    navigationIcon = {
                        Surface ( modifier = Modifier.padding(PaddingValues(if(navController.currentBackStackEntryAsState().value?.destination?.route?.contains("search") == false) 12.dp else 0.dp)) ) {
                            if (navController.currentBackStackEntryAsState().value?.destination?.route?.contains("details/") == true) {
                                IconButton(content = {
                                    Icon(
                                        Icons.AutoMirrored.Rounded.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                },
                                    onClick = { navController.popBackStack() })
                            } else if (navController.currentBackStackEntryAsState().value?.destination?.route?.contains("search") == false) {
                                Icon(Icons.Rounded.Train, contentDescription = "Train")
                            } else {
                                IconButton(content = {
                                    Icon(
                                        Icons.Rounded.Close,
                                        contentDescription = "Close Search"
                                    )
                                },
                                    onClick = {
                                        navController.popBackStack()
                                        searchQuery = ""
                                        searchSuggestions = listOf()
                                    })
                            }
                        }
                    },
                    actions = {
                        IconButton(content = { Icon(Icons.Rounded.Search, contentDescription = "Search") },
                            onClick = {
                                navController.navigate("search")
                            })
                        IconButton(content = { Icon(Icons.Rounded.Refresh, contentDescription = "Reload") },
                            onClick = { reloadTrigger = !reloadTrigger })
                    }
                )
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
                    favouriteStations = favouriteStations,
                    searchSuggestions = searchSuggestions,
                    setStationId = { newId ->
                        stationId = newId
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

