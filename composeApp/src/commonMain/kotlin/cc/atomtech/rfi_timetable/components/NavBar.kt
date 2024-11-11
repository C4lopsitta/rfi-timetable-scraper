package cc.atomtech.rfi_timetable.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
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
fun NavBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(label = { Text("Departures") },
            icon = { Icon(Icons.Rounded.ArrowUpward, contentDescription = "Departures") },
            selected = isCurrentRoute(navController, "departures"),
            onClick = {
                navController.navigate("departures")
            })
        NavigationBarItem(label = { Text("Arrivals") },
            icon = { Icon(Icons.Rounded.ArrowDownward, contentDescription = "Arrivals") },
            selected = isCurrentRoute(navController, "arrivals"),
            onClick = {
                navController.navigate("arrivals")
            })
        NavigationBarItem(label = { Text("Favourite Stations") },
            icon = { Icon(Icons.Rounded.Star, contentDescription = "Star") },
            selected = isCurrentRoute(navController, "favourites"),
            onClick = {
                navController.navigate("favourites")
            })
    }
}
