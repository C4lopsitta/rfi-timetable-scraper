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
import cc.atomtech.timetable.models.TrainData
import cc.atomtech.timetable.views.Timetable
import cc.atomtech.timetable.views.TrainDetails
import androidx.compose.ui.text.font.FontWeight
import cc.atomtech.timetable.AppPreferences
import cc.atomtech.timetable.views.AppInfo
import cc.atomtech.timetable.views.BookmarkedStations
import cc.atomtech.timetable.views.notices.InfoLavori
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.models.DetailedTrainData
import cc.atomtech.timetable.models.TrenitaliaInfoLavori
import cc.atomtech.timetable.models.viewmodels.Station
import cc.atomtech.timetable.views.CercaTreno
import cc.atomtech.timetable.views.management.Settings
import cc.atomtech.timetable.views.StationSearch
import cc.atomtech.timetable.views.management.WhatsNew
import cc.atomtech.timetable.views.notices.TrenitaliaRegionInfo

@Composable
fun NavigationBodyHost(
    stationData: Station,
    navController: NavHostController,
    isDesktop: Boolean,
    tabIndex: Int,
    preferences: AppPreferences,
    setStationId: (Int) -> Unit,
) {
    var detailViewSelectedTrain by remember { mutableStateOf<DetailedTrainData?>(null) }
    var selectedRegionInfo by remember { mutableStateOf<TrenitaliaInfoLavori?>(null) }

    // todo)) Rework
    fun viewTrainDetails(pick: TrainData, pickedFromArrival: Boolean) {
        TODO()
//        if(stationData.currentStation.value == null) return
//        val pickTwin = (if(!pickedFromArrival) timetable.arrivals else timetable.departures).find {
//            it.number == pick.number
//        }
//
//        val arrivalData = if(pickTwin != null) (if(!pickedFromArrival) pickTwin else pick) else pick
//        val departureData = if(pickTwin != null) (if(pickedFromArrival) pickTwin else pick) else pick
//
//        val stops = arrayListOf<TrainStop>()
//
//        stops.add(
//            TrainStop(
//                name = timetable.stationName,
//                time = pick.time ?: StringRes.get("undefined"),
//                isCurrentStop = true
//            )
//        )
//
//        pick.stops.forEach { stop -> stops.add(stop) }
//
//        detailViewSelectedTrain = DetailedTrainData(
//            currentStationType = if(pickTwin != null) {
//                CurrentStationType.STOP
//            } else if(pickedFromArrival) {
//                CurrentStationType.LINE_END
//            } else {
//                CurrentStationType.LINE_START
//            },
//            departure = departureData.station ?: StringRes.get("undefined"),
//            arrival = arrivalData.station ?: StringRes.get("undefined"),
//            departsAt = departureData.time ?: "--:--",
//            arrivesAt = arrivalData.time ?: "--:--",
//            stops = stops.toList(),
//            delay = departureData.getDelayString(addSpace = false),
//            delayMinutes = departureData.delay,
//            operator = departureData.operator,
//            category = departureData.category,
//            platform = pick.platform,
//            details = pick.details,
//            number = pick.number ?: StringRes.get("undefined"),
//        )
//
//        navController.navigate("details/${if(pickedFromArrival) "true" else "false"}")
    }

    Column {
        if(isDesktop) {
            Row (
                modifier = Modifier.padding( vertical = 20.dp, horizontal = 16.dp ),
            ) {
                Text(stationData.currentStation.value?.name ?: StringRes.get("app_name"),
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
                    trainList = stationData.departures.value,
                    stationInfo = stationData.info.value,
                    isDesktop = isDesktop
                )
            }

            composable("arrivals") {
                Timetable(
                    trainList = stationData.arrivals.value,
                    stationInfo = stationData.info.value,
                    isDesktop = isDesktop
                )
            }
            composable("favourites") {
                BookmarkedStations( stationData )
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
                StationSearch(
                    stationData = stationData,
                    navController = navController
                )
            }

            composable("settings") {
                Settings(preferences = preferences)
            }

            composable("whatsnew") {
                WhatsNew()
            }

            composable("cerca_treno") {
                CercaTreno()
            }

            composable("details/{isArrival}") {
                val isArrival = it.arguments?.getString("isArrival") == "true"
                TrainDetails(detailViewSelectedTrain!!, isArrival)
            }
            composable("info") { AppInfo() }
        }
    }
}