package cc.atomtech.timetable.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Engineering
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
private fun isCurrentRoute(navController: NavHostController, route: String): Boolean {
    return navController.currentBackStackEntryAsState().value?.destination?.route == route
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
fun NavRail(navController: NavHostController,
            triggerReload: () -> Unit) {

    NavigationRail (
        modifier = Modifier.fillMaxHeight(),
    ) {
        Icon(
            contentDescription = "Timetables",
            imageVector = Icons.Rounded.Train,
            modifier = Modifier.padding(vertical = 24.dp)
        )
        HorizontalDivider( modifier = Modifier.width( 32.dp ).padding( bottom = 12.dp ) )
        NavigationRailItem(
            label = { Text("Search") },
            icon = {
                val isCurrentRoute = isCurrentRoute(navController, "search")
                HighlightedIcon(
                    { Icon(Icons.Rounded.Search, contentDescription = "Search") },
                    { Icon(Icons.Filled.Search, contentDescription = "Search") },
                    isCurrentRoute
                )
            },
            selected = isCurrentRoute(navController, "search"),
            onClick = {
                navController.navigate("search")
            }
        )
        NavigationRailItem(
                label = { Text("Refresh") },
        icon = { Icon(Icons.Rounded.Refresh, contentDescription = "Refresh") },
        selected = false,
        onClick = {
            triggerReload()
        }
        )
        HorizontalDivider( modifier = Modifier.width( 32.dp ).padding( vertical = 12.dp ) )
        NavigationRailItem(
            label = { Text("Departures") },
            icon = {
                val isCurrentRoute = isCurrentRoute(navController, "departures")
                HighlightedIcon(
                    { Icon(Icons.Rounded.ArrowUpward, contentDescription = "Departures") },
                    { Icon(Icons.Filled.ArrowUpward, contentDescription = "Departures") },
                    isCurrentRoute
                )
            },
            selected = isCurrentRoute(navController, "departures"),
            onClick = {
                navController.navigate("departures")
            }
        )
        Spacer(Modifier.height(12.dp))
        NavigationRailItem(
            label = { Text("Arrivals") },
            icon = {
                val isCurrentRoute = isCurrentRoute(navController, "arrivals")
                HighlightedIcon(
                    { Icon(Icons.Rounded.ArrowDownward, contentDescription = "Arrivals") },
                    { Icon(Icons.Filled.ArrowDownward, contentDescription = "Arrivals") },
                    isCurrentRoute
                )
            },
            selected = isCurrentRoute(navController, "arrivals"),
            onClick = {
                navController.navigate("arrivals")
            }
        )
        Spacer(Modifier.height(12.dp))
        NavigationRailItem(
            label = { Text("Stations") },
            icon = {
                val isCurrentRoute = isCurrentRoute(navController, "favourites")
                HighlightedIcon(
                    { Icon(Icons.Rounded.Star, contentDescription = "Favourites") },
                    { Icon(Icons.Filled.Star, contentDescription = "Favourites") },
                    isCurrentRoute
                )
            },
            selected = isCurrentRoute(navController, "favourites"),
            onClick = {
                navController.navigate("favourites")
            }
        )
        Spacer(Modifier.height(12.dp))
        NavigationRailItem(
            label = { Text("Rail Works") },
            icon = {
                val isCurrentRoute = isCurrentRoute(navController, "infolavori")
                HighlightedIcon(
                    { Icon(Icons.Rounded.Engineering, contentDescription = "Rail Works") },
                    { Icon(Icons.Filled.Engineering, contentDescription = "Rail Works") },
                    isCurrentRoute
                )
            },
            selected = isCurrentRoute(navController, "infolavori"),
            onClick = {
                navController.navigate("infolavori")
            }
        )
        Spacer(Modifier.height(12.dp))
    }
}