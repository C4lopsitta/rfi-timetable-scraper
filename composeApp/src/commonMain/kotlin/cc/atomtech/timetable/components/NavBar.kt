package cc.atomtech.timetable.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.Engineering
import androidx.compose.material.icons.rounded.LocationCity
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import cc.atomtech.timetable.Routes
import cc.atomtech.timetable.StringRes

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

@Composable
fun NavBar(navController: NavHostController) {
    NavigationBar {
        // region oldRoutes
        NavigationBarItem(label = { Text(StringRes.get("nav_departures")) },
            icon = {
                val isCurrentRoute = isCurrentRoute(navController, "departures")
                HighlightedIcon(
                    { Icon(Icons.Rounded.ArrowUpward, contentDescription = StringRes.get("nav_departures")) },
                    { Icon(Icons.Filled.ArrowUpward, contentDescription = StringRes.get("nav_departures")) },
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
                    { Icon(Icons.Rounded.ArrowDownward, contentDescription = StringRes.get("nav_arrivals")) },
                    { Icon(Icons.Filled.ArrowDownward, contentDescription = StringRes.get("nav_arrivals")) },
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
        NavigationBarItem(label = { Text(StringRes.get("nav_favourites")) },
            icon = {
                val isCurrentRoute = isCurrentRoute(navController, "favourites")
                HighlightedIcon(
                    { Icon(Icons.Rounded.Star, contentDescription = StringRes.get("nav_favourites")) },
                    { Icon(Icons.Filled.Star, contentDescription = StringRes.get("nav_favourites")) },
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
        NavigationBarItem(
            label = { Text(StringRes.get("nav_settings")) },
            icon = { Icon(Icons.Rounded.Settings, contentDescription = StringRes.get("nav_icon_settings")) },
            selected = isCurrentRoute(navController, "settings"),
            onClick = {
                navController.navigate("settings") {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        )
        // endregion oldRoutes
    }
}
