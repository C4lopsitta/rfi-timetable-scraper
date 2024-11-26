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
import cc.atomtech.timetable.Strings

@Composable
private fun isCurrentRoute(navController: NavHostController, route: String): Boolean {
    return navController.currentBackStackEntryAsState().value?.destination?.route?.contains(route) ?: false
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
        NavigationBarItem(label = { Text(Strings.get("nav_departures")) },
            icon = {
                val isCurrentRoute = isCurrentRoute(navController, "departures")
                HighlightedIcon(
                    { Icon(Icons.Rounded.ArrowUpward, contentDescription = Strings.get("nav_departures")) },
                    { Icon(Icons.Filled.ArrowUpward, contentDescription = Strings.get("nav_departures")) },
                    isCurrentRoute
                )
            },
            selected = isCurrentRoute(navController, "departures"),
            onClick = {
                navController.navigate("departures")
            })
        NavigationBarItem(label = { Text(Strings.get("nav_arrivals")) },
            icon = {
                val isCurrentRoute = isCurrentRoute(navController, "arrivals")
                HighlightedIcon(
                    { Icon(Icons.Rounded.ArrowDownward, contentDescription = Strings.get("nav_arrivals")) },
                    { Icon(Icons.Filled.ArrowDownward, contentDescription = Strings.get("nav_arrivals")) },
                    isCurrentRoute
                )
            },
            selected = isCurrentRoute(navController, "arrivals"),
            onClick = {
                navController.navigate("arrivals")
            })
        NavigationBarItem(label = { Text(Strings.get("nav_favourites")) },
            icon = {
                val isCurrentRoute = isCurrentRoute(navController, "favourites")
                HighlightedIcon(
                    { Icon(Icons.Rounded.Star, contentDescription = Strings.get("nav_favourites")) },
                    { Icon(Icons.Filled.Star, contentDescription = Strings.get("nav_favourites")) },
                    isCurrentRoute
                )
            },
            selected = isCurrentRoute(navController, "favourites"),
            onClick = {
                navController.navigate("favourites")
            })
        NavigationBarItem(label = { Text(Strings.get("nav_works")) },
            icon = {
                val isCurrentRoute = isCurrentRoute(navController, "infolavori")
                HighlightedIcon(
                    { Icon(Icons.Rounded.Engineering, contentDescription = Strings.get("nav_works")) },
                    { Icon(Icons.Filled.Engineering, contentDescription = Strings.get("nav_works")) },
                    isCurrentRoute
                )
            },
            selected = isCurrentRoute(navController, "infolavori"),
            onClick = {
                navController.navigate("infolavori")
            })
    }
}
