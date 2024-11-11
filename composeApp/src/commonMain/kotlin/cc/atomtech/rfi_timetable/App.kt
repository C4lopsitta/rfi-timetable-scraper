package cc.atomtech.rfi_timetable

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.*
import androidx.navigation.compose.*
import androidx.navigation.compose.NavHost
import cc.atomtech.rfi_timetable.components.NavBar
import cc.atomtech.rfi_timetable.models.Station
import cc.atomtech.rfi_timetable.models.Stations
import cc.atomtech.rfi_timetable.models.TimetableState
import cc.atomtech.rfi_timetable.models.TrainData
import cc.atomtech.rfi_timetable.views.Timetable
import cc.atomtech.rfi_timetable.views.TrainDetails
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun Main(navController: NavHostController) {
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    var searchSuggestions by remember { mutableStateOf<List<Station>?>(null) }
    val stations by remember { mutableStateOf(Stations(listOf())) }
    var stationId by remember { mutableStateOf(1728) }
    var timetable by remember { mutableStateOf<TimetableState?>(null) }
    var detailViewSelectedTrain by remember { mutableStateOf<TrainData?>(null) }

    LaunchedEffect(Unit) {
        try {
            stations.stations = RfiScraper.getStations()
        } catch (e: Exception) {
            println(e.printStackTrace())
            error = e.toString()
        }
    }

    LaunchedEffect(stationId) {
        try {
            loading = true
            timetable = RfiScraper.getStationTimetable(stationId)
            loading = false
        } catch (e: Exception) {
            println(e.printStackTrace())
            error = e.toString()
        }
    }

    if(error != null) {
        AlertDialog(
            title = { Text("Data fetch error") },
            text = { Text(error.toString()) },
            buttons = {
                ElevatedButton(
                    content = { Text("Ok") },
                    onClick = {
                        error = null
                    }
                )
            },
            onDismissRequest = {
                error = null
            }
        )
    }

    val colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    MaterialTheme (
        colorScheme = colorScheme
    ) {
        Scaffold (
            topBar = {
                TopAppBar(
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
                                maxLines = 1
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
                                    onClick = { navController.popBackStack() })
                            }
                        }
                    },
                    actions = {
                        IconButton(content = { Icon(Icons.Rounded.Search, contentDescription = "Search") },
                            onClick = {
                                navController.navigate("search")
                            })
                    }
                )
            },
            bottomBar = { NavBar(navController) }
        ) { paddingValues ->
            Surface (
                modifier = Modifier.padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                ).fillMaxWidth()
            ) {
                if(loading)
                    LinearProgressIndicator()
                NavHost(
                    navController = navController,
                    startDestination = "departures",
                    modifier = Modifier.padding(start = 12.dp)
                ) {
                    composable("departures") {
                        if (!loading) {
                            Timetable(
                                trainList = timetable?.uiState?.value?.departures,
                                colorScheme = colorScheme,
                                onTrainSelected = { selectedTrain: TrainData ->
                                    detailViewSelectedTrain = selectedTrain
                                    navController.navigate("details/false")
                                }
                            )
                        }
                    }
                    composable("arrivals") {
                        if (!loading) {
                            Timetable(
                                trainList = timetable?.uiState?.value?.arrivals,
                                colorScheme = colorScheme,
                                onTrainSelected = { selectedTrain: TrainData ->
                                    detailViewSelectedTrain = selectedTrain
                                    navController.navigate("details/true")
                                }
                            )
                        }
                    }
                    composable("favourites") {}
                    composable("search") {
                        if(stations.stations.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp)
                            ) {
                                items(searchSuggestions ?: listOf()) { suggestion ->
                                    Text(suggestion.name,
                                        fontSize = 24.sp,
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(vertical = 12.dp)
                                            .clickable(interactionSource = remember { MutableInteractionSource() },
                                                indication = LocalIndication.current,
                                                role  = Role.Button,
                                                onClickLabel = "Click to open details",
                                                onClick = {
                                                    stationId = suggestion.id
                                                    navController.popBackStack()
                                                }
                                            ))
                                    HorizontalDivider()
                                }
                            }
                        }
                    }
                    composable("details/{isArrival}") {
                        val isArrival = it.arguments?.getString("isArrival") == "true"
                        TrainDetails(detailViewSelectedTrain, isArrival)
                    }
                }
            }

        }
    }
}


//@Composable
//fun AppNavHost(navController: NavHostController) {
//    val timetable by remember { mutableStateOf<TimetableState?>(null) }
//
//    NavHost(
//        navController = navController,
//        startDestination = "main"
//    ) {
//        composable("main") { Main(navController) }
//        composable("trainDetails/{type}/{number}") { backStateEntry ->
//            TrainDetails(
//                backStateEntry.arguments?.getString("type") == "arrivals",
//                backStateEntry.arguments?.getString("number"),
//                timetable)
//        }
//    }

