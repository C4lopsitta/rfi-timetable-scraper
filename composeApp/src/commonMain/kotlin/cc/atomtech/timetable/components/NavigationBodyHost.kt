package cc.atomtech.timetable.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cc.atomtech.timetable.models.Station
import cc.atomtech.timetable.models.Stations
import cc.atomtech.timetable.models.TimetableState
import cc.atomtech.timetable.models.TrainData
import cc.atomtech.timetable.views.Timetable
import cc.atomtech.timetable.views.TrainDetails
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.navigation.compose.currentBackStackEntryAsState
import cc.atomtech.timetable.views.AppInfo
import cc.atomtech.timetable.views.DesktopSearch
import cc.atomtech.timetable.views.FavouriteStations
import cc.atomtech.timetable.views.InfoLavori
import cc.atomtech.timetable.views.MobileSearch

@Composable
fun NavigationBodyHost(
    navController: NavHostController,
    isDesktop: Boolean,
    isLoading: Boolean,
    timetable: TimetableState?,
    stations: Stations,
    favouriteStations: Stations,
    searchSuggestions: List<Station>?,
    setStationId: (Int) -> Unit,
    updateFavourites: (String) -> Unit) {
    var detailViewSelectedTrain by remember { mutableStateOf<TrainData?>(null) }

    Column {
        if(isDesktop) {
            Row (
                modifier = Modifier.padding( vertical = 20.dp, horizontal = 16.dp ),
            ) {
                Text(timetable?.stationName ?: "Timetable",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        NavHost(
            navController = navController,
            startDestination = "departures",
            modifier = Modifier.padding(start = 12.dp)
        ) {

            composable("departures") {
                Timetable(
                    trainList = timetable?.uiState?.value?.departures,
                    onTrainSelected = { selectedTrain: TrainData ->
                        detailViewSelectedTrain = selectedTrain
                        navController.navigate("details/false")
                    },
                    stationInfo = timetable?.uiState?.value?.stationInfo,
                    lastUpdate = timetable?.uiState?.value?.lastUptade ?: 0,
                    isDesktop = isDesktop
                )
            }
            composable("arrivals") {
                Timetable(
                    trainList = timetable?.uiState?.value?.arrivals,
                    onTrainSelected = { selectedTrain: TrainData ->
                        detailViewSelectedTrain = selectedTrain
                        navController.navigate("details/true")
                    },
                    stationInfo = timetable?.uiState?.value?.stationInfo,
                    lastUpdate = timetable?.uiState?.value?.lastUptade ?: 0,
                    isDesktop = isDesktop
                )
            }
            composable("favourites") {
                FavouriteStations(
                    favouriteStations,
                    setStation = {
                        setStationId(it)
                        navController.navigate("departures")
                    }
                )
            }
            composable("infolavori") { InfoLavori() }
            composable("search") {
                if (isDesktop) {
                    DesktopSearch(
                        navController = navController,
                        stations = stations
                    ) { setStationId(it) }
                } else {
                    MobileSearch(
                        stations = stations,
                        searchSuggestions = searchSuggestions,
                        favouriteStations = favouriteStations,
                        navController = navController,
                        updateFavourites = updateFavourites
                    ) { setStationId(it) }
                }
            }
            composable("details/{isArrival}") {
                val isArrival = it.arguments?.getString("isArrival") == "true"
                TrainDetails(detailViewSelectedTrain, isArrival)
            }
            composable("info") { AppInfo() }
        }
    }
}