package cc.atomtech.timetable.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.text.font.FontWeight
import cc.atomtech.timetable.views.AppInfo
import cc.atomtech.timetable.views.DesktopSearch
import cc.atomtech.timetable.views.FavouriteStations
import cc.atomtech.timetable.views.InfoLavori
import cc.atomtech.timetable.views.MobileSearch
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.enumerations.CurrentStationType
import cc.atomtech.timetable.models.DetailedTrainData
import cc.atomtech.timetable.models.TrainStop
import cc.atomtech.timetable.models.TrenitaliaInfoLavori
import cc.atomtech.timetable.views.TrenitaliaRegionInfo

@Composable
fun NavigationBodyHost(
    navController: NavHostController,
    isDesktop: Boolean,
    isLoading: Boolean,
    tabIndex: Int,
    timetable: TimetableState?,
    stations: Stations,
    favouriteStations: Stations,
    searchSuggestions: List<Station>?,
    setStationId: (Int) -> Unit,
    updateFavourites: (String) -> Unit
) {
    var detailViewSelectedTrain by remember { mutableStateOf<DetailedTrainData?>(null) }
    var selectedRegionInfo by remember { mutableStateOf<TrenitaliaInfoLavori?>(null) }

    fun viewTrainDetails(pick: TrainData, pickedFromArrival: Boolean) {
        if(timetable == null) return
        val pickTwin = (if(!pickedFromArrival) timetable.arrivals else timetable.departures).find {
            it.number == pick.number
        }

        val arrivalData = if(pickTwin != null) (if(!pickedFromArrival) pickTwin else pick) else pick
        val departureData = if(pickTwin != null) (if(pickedFromArrival) pickTwin else pick) else pick

        val stops = arrayListOf<TrainStop>()

        stops.add(
            TrainStop(
                name = timetable.stationName,
                time = pick.time ?: StringRes.get("undefined"),
                isCurrentStop = true
            )
        )

        pick.stops.forEach { stop -> stops.add(stop) }

        detailViewSelectedTrain = DetailedTrainData(
            currentStationType = if(pickTwin != null) {
                CurrentStationType.STOP
            } else if(pickedFromArrival) {
                CurrentStationType.LINE_END
            } else {
                CurrentStationType.LINE_START
            },
            departure = departureData.station ?: StringRes.get("undefined"),
            arrival = arrivalData.station ?: StringRes.get("undefined"),
            departsAt = departureData.time ?: "--:--",
            arrivesAt = arrivalData.time ?: "--:--",
            stops = stops.toList(),
            delay = departureData.getDelayString(addSpace = false),
            operator = departureData.operator.toString(),
            category = departureData.category.toString(),
            platform = pick.platform,
            details = pick.details,
            number = pick.number ?: StringRes.get("undefined"),
        )

        navController.navigate("details/${if(pickedFromArrival) "true" else "false"}")
    }

    Column {
        if(isDesktop) {
            Row (
                modifier = Modifier.padding( vertical = 20.dp, horizontal = 16.dp ),
            ) {
                Text(timetable?.stationName ?: StringRes.get("app_name"),
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
                    onTrainSelected = { selectedTrain: TrainData -> viewTrainDetails(selectedTrain, false) },
                    stationInfo = timetable?.uiState?.value?.stationInfo,
                    lastUpdate = timetable?.uiState?.value?.lastUptade ?: 0,
                    isDesktop = isDesktop
                )
            }
            composable("arrivals") {
                Timetable(
                    trainList = timetable?.uiState?.value?.arrivals,
                    onTrainSelected = { selectedTrain: TrainData -> viewTrainDetails(selectedTrain, true) },
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
            composable("infolavori") {
                InfoLavori(
                    tabIndex = tabIndex,
                ) { regionInfo ->
                    selectedRegionInfo = regionInfo
                    navController.navigate("infolavori/regionInfo")
                }
            }
            composable("infolavori/regionInfo") {
                TrenitaliaRegionInfo(
                    selectedRegionInfo,
                    navController
                )
            }
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
                TrainDetails(detailViewSelectedTrain!!, isArrival)
            }
            composable("info") { AppInfo() }
        }
    }
}