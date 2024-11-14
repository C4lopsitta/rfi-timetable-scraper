package cc.atomtech.timetable.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Engineering
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun NavBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(label = { Text("Departures") },
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
            })
        NavigationBarItem(label = { Text("Arrivals") },
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
            })
        NavigationBarItem(label = { Text("Favourites") },
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
            })
        NavigationBarItem(label = { Text("Rail Works") },
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
            })
    }
}
