package cc.atomtech.timetable.components.navbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuOpen
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.SouthEast
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Engineering
import androidx.compose.material.icons.rounded.NorthEast
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SouthEast
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.models.rfi.StationBaseData

@Composable
private fun isCurrentRoute(navController: NavHostController, route: String): Boolean {
    // TODO)) Fix
    return (navController.currentBackStackEntryAsState().value?.destination?.route?.split("/")?.get(0) ?: "")
        .contains(route, ignoreCase = true)
}

@Composable
private fun HighlightedIcon(icon: @Composable () -> Unit, highlighed: @Composable () -> Unit, condition: Boolean) {
    if( condition ) {
        icon()
    } else {
        highlighed()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(
    navController: NavHostController,
    station: State<StationBaseData?>
) {
    var showOverflow by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )


    NavigationBar {
        // region oldRoutes
        NavigationBarItem(label = { Text(StringRes.get("nav_departures")) },
            icon = {
                val isCurrentRoute = isCurrentRoute(navController, "departures")
                HighlightedIcon(
                    { Icon(Icons.Rounded.NorthEast, contentDescription = StringRes.get("nav_departures")) },
                    { Icon(Icons.Filled.NorthEast, contentDescription = StringRes.get("nav_departures")) },
                    isCurrentRoute
                )
            },
            selected = isCurrentRoute(navController, "departures"),
            onClick = {
                navController.navigate("departures")  {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            })
        NavigationBarItem(label = { Text(StringRes.get("nav_arrivals")) },
            icon = {
                val isCurrentRoute = isCurrentRoute(navController, "arrivals")
                HighlightedIcon(
                    { Icon(Icons.Rounded.SouthEast, contentDescription = StringRes.get("nav_arrivals")) },
                    { Icon(Icons.Filled.SouthEast, contentDescription = StringRes.get("nav_arrivals")) },
                    isCurrentRoute
                )
            },
            selected = isCurrentRoute(navController, "arrivals"),
            onClick = {
                navController.navigate("arrivals") {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            })
        NavigationBarItem(
            label = { Text(StringRes.get("nav_favourites")) },
            icon = {
                val isCurrentRoute = isCurrentRoute(navController, "favourites")
                HighlightedIcon(
                    { Icon(Icons.Rounded.Bookmarks, contentDescription = StringRes.get("nav_favourites")) },
                    { Icon(Icons.Filled.Bookmarks, contentDescription = StringRes.get("nav_favourites")) },
                    isCurrentRoute
                )
            },
            selected = isCurrentRoute(navController, "favourites"),
            onClick = {
                navController.navigate("favourites") {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            })
        NavigationBarItem(label = { Text(StringRes.get("nav_notices")) },
            icon = {
                val isCurrentRoute = isCurrentRoute(navController, "infolavori")
                HighlightedIcon(
                    { Icon(Icons.Rounded.Engineering, contentDescription = StringRes.get("nav_icon_notices")) },
                    { Icon(Icons.Filled.Engineering, contentDescription = StringRes.get("nav_icon_notices")) },
                    isCurrentRoute
                )
            },
            selected = isCurrentRoute(navController, "infolavori"),
            onClick = {
                navController.navigate("infolavori") {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            })
//        NavigationBarItem(
//            label = { Text(StringRes.get("nav_settings")) },
//            icon = { Icon(Icons.Rounded.Settings, contentDescription = StringRes.get("nav_icon_settings")) },
//            selected = isCurrentRoute(navController, "settings"),
//            onClick = {

//            }
//        )

        NavigationBarItem(
            label = { Text(StringRes.get("nav_other")) },
            icon = {
                Icon(
                    Icons.AutoMirrored.Rounded.MenuOpen,
                    contentDescription = StringRes.get("nav_icon_other")
                )
            },
            selected = showOverflow || (
                    !isCurrentRoute(navController, "departures") &&
                    !isCurrentRoute(navController, "arrivals") &&
                    !isCurrentRoute(navController, "infolavori") &&
                    !isCurrentRoute(navController, "favourites")
            ),
            onClick = {
                showOverflow = true
            }
        )
        // endregion oldRoutes
    }

    if(showOverflow) ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            showOverflow = false
        }
    ) {
        Column (
            modifier = Modifier.padding( horizontal = 12.dp )
        ) {
            OverflowNavItem(
                icon = { Icon(Icons.Rounded.Schedule, contentDescription = StringRes.get("nav_icon_schedule")) },
                text = StringRes.get("nav_schedule"),
                isDisabled = true
            ) {
                navController.navigate("schedule")
                showOverflow = false
            }
            OverflowNavItem(
                icon = { Icon(Icons.Rounded.Train, contentDescription = StringRes.get("nav_icon_station_info")) },
                text = if(station.value != null) StringRes.format("nav_station_info", station.value!!.name) else StringRes.get("nav_station_info_nostation"),
                isDisabled = true// station == null
            ) {
                navController.navigate("station_info")
                showOverflow = false
            }
            OverflowNavItem(
                icon = { Icon(Icons.Rounded.Route, contentDescription = StringRes.get("nav_icon_cerca_treno")) },
                text = StringRes.get("nav_cerca_treno"),
            ) {
                navController.navigate("cerca_treno")
                showOverflow = false
            }
            OverflowNavItem(
                icon = { Icon(Icons.Rounded.Settings, contentDescription = StringRes.get("nav_icon_settings")) },
                text = StringRes.get("nav_settings"),
            ) {
                navController.navigate("settings")
                showOverflow = false
            }
            OverflowNavItem(
                icon = { Icon(Icons.Rounded.AutoAwesome, contentDescription = StringRes.get("nav_icon_whatsnew")) },
                text = StringRes.get("nav_whatsnew")
            ) {
                navController.navigate("whatsnew")
                showOverflow = false
            }
            Box( modifier = Modifier.height( 64.dp ) )
        }
    }
}
