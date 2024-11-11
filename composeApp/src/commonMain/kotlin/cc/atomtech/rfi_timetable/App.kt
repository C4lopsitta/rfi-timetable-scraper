package cc.atomtech.rfi_timetable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.*
import androidx.navigation.compose.NavHost
import cc.atomtech.rfi_timetable.components.NavBar
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
    var isSearching by remember { mutableStateOf(false) }
    var stationId by remember { mutableStateOf(1728) }
    var timetable by remember { mutableStateOf<TimetableState?>(null) }
    var detailViewSelectedTrain by remember { mutableStateOf<TrainData?>(null) }


    @Composable
    fun loadData() {
        LaunchedEffect(Unit) {
            try {
                timetable = RfiScraper.getStationTimetable(stationId)
                loading = false
            } catch (e: Exception) {
                println(e.printStackTrace())
                error = e.toString()
            }
        }
    }
    loadData()

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
                        if(!isSearching) {
                            Text(timetable?.stationName ?: "Timetables")
                        } else {
                            TextField(value = "",
                                onValueChange = {

                                },
                                placeholder = { Text("Search") }
                            )
                        }
                    },
                    navigationIcon = {
                        Surface ( modifier = Modifier.padding(PaddingValues(if(!isSearching) 12.dp else 0.dp)) ) {
                            if(!isSearching) {
                                Icon(Icons.Rounded.Train, contentDescription = "Train")
                            } else {
                                IconButton(content = { Icon(Icons.Rounded.Close, contentDescription = "Close Search") },
                                    onClick = { isSearching = false })
                            }
                        }
                    },
                    actions = {
                        IconButton(content = { Icon(Icons.Rounded.Search, contentDescription = "Search") },
                            onClick = {
                                if(!isSearching) {
                                    isSearching = true
                                    return@IconButton
                                }

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
                    start = 12.dp
                ).fillMaxWidth()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "departures"
                ) {
                    composable("departures") {
                        if (!loading) {
                            Timetable(
                                trainList = timetable?.uiState?.value?.departures,
                                colorScheme = colorScheme,
                                onTrainSelected = { selectedTrain: TrainData ->
                                    detailViewSelectedTrain = selectedTrain
                                    navController.navigate("details")
                                }
                            )
                        } else {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(PaddingValues(64.dp))
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    composable("arrivals") {
                        if (!loading) {
                            Timetable(
                                trainList = timetable?.uiState?.value?.arrivals,
                                colorScheme = colorScheme,
                                onTrainSelected = { selectedTrain: TrainData ->
                                    detailViewSelectedTrain = selectedTrain
                                    navController.navigate("details")
                                }
                            )
                        } else {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(PaddingValues(64.dp))
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    composable("favourites") {}
                    composable("details") { TrainDetails(detailViewSelectedTrain) }
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

