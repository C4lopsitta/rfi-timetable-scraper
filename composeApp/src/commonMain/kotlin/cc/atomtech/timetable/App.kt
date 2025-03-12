package cc.atomtech.timetable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.*
import androidx.navigation.compose.currentBackStackEntryAsState
import cc.atomtech.timetable.components.navbar.NavBar
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
import cc.atomtech.timetable.enumerations.Platform
import cc.atomtech.timetable.models.rfi.StationBaseData
import cc.atomtech.timetable.preferences.MutablePreference
import cc.atomtech.timetable.scrapers.RfiScraper
import cc.atomtech.timetable.scrapers.RssFeeds
import kotlinx.coroutines.IO


const val preferencesFile = "timetables-prefs.preferences_pb"

expect val platform: Platform;

expect object AppVersion {
    val versionCode: Int
    val versionName: String
}

expect fun toggleStrikesNotificationService(
    isEnabled: Boolean,
    runningHour: Int = 18
);
expect fun debugRunStrikesNotificationService()

@Composable expect fun storePreferences(): DataStore<Preferences>

//@Composable expect fun isNetworkAvailable(): Boolean

fun instantiatePreferences(createPath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            createPath().toPath()
        }
    )

@Composable
@Preview
fun Main(navController: NavHostController,
         isDesktop: Boolean = false,
         preferences: AppPreferences,
         colorScheme: ColorScheme? = null,
         stationData: cc.atomtech.timetable.models.viewmodels.Station = cc.atomtech.timetable.models.viewmodels.Station(preferences)) {
    val snackbarHostState = SnackbarHostState()


    // region preferences
    // TODO)) Use these setters in Settings

    var autoReloadIntervalMinutes by remember {
        MutablePreference<Int>(initialValue = 5, coroutineScope = CoroutineScope(Dispatchers.IO)) {
            preferences.setReloadDelay(it)
        }
    }

    // Load preferences
    LaunchedEffect(Unit) {
        autoReloadIntervalMinutes = preferences.getReloadDelay().first()
    }
    // endregion preferences

    var error by remember { mutableStateOf<String?>(null) }
    var tabIndex by remember { mutableStateOf(0) }

    // TODO)) Kill
    // region toKill
    
    var timetableRefresher: Job? = null
    
    // endregion toKill


    // TODO)) Crucify function
//    LaunchedEffect(Unit) {
//        try {
//            favouriteStations = Stations.fromFavourites(preferences.getFavouriteStations().first(), stationData.allStationData.value)
//        } catch (e: Exception) {
//            println(e.printStackTrace())
//            error = e.toString()
//        }
//    }

    // TODO)) Move RSS Feeds to their own ViewModel
    LaunchedEffect(Unit) {
        try {
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
                        SnackbarResult.ActionPerformed -> {
                            stationData.update()
                        }
                    }
                }
            }
        } finally {
//            timetableRefresher?.cancel()
//            // stores how many 5 minutes it has to wait, so multiply by 5 to get actual value, if 0 skip reload
//            autoReloadIntervalMinutes = preferences.getReloadDelay().first() * 5
//            if(autoReloadIntervalMinutes > 0) {
//                timetableRefresher = CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
//                    delay((autoReloadIntervalMinutes * 60 * 1000).toLong())
//                    reloadTrigger = !reloadTrigger
//                }
//            }
        }
    }

    val actualColorScheme = if(colorScheme == null) {
        if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    } else colorScheme


    MaterialTheme (
        colorScheme = actualColorScheme,
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
                            stationData = stationData,
                            triggerReload = { stationData.update() }
                        )
                    }
                }
            },
            snackbarHost = {
                SnackbarHost( snackbarHostState )
            },
            bottomBar = { if(!isDesktop) NavBar(navController, station = stationData.currentStation) },
            floatingActionButton = {

            }
        ) { paddingValues ->
            if( stationData.loadingArrivals.value || stationData.loadingDepartures.value || stationData.loadingStations.value )
                LinearProgressIndicator( modifier = Modifier.fillMaxWidth() )

            ScaffoldBody(
                isDesktop = isDesktop,
                paddingValues = paddingValues,
                navRail = {
                    if (isDesktop) {
                        NavRail(navController = navController) { stationData.update() }
                    }
                }
            ) {
                NavigationBodyHost(
                    navController = navController,
                    isDesktop = isDesktop,
                    stationData = stationData,
                    tabIndex = tabIndex,
                    preferences = preferences,
                    setStationId = { newId ->
                        stationData.updateStationById(newId)
                        CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
                            preferences.setStationId(newId)
                            navController.navigate("departures")
                        }
                    }
                )
            }
        }
    }
}
