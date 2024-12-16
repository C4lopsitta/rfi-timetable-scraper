package cc.atomtech.timetable.components.navbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NorthEast
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SouthWest
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.NorthEast
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.SouthWest
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import cc.atomtech.timetable.Routes
import cc.atomtech.timetable.StringRes


@Composable
fun StationNavBar(navController: NavHostController) {
    val navItems = listOf(
        NavItem(
            StringRes.get("nav_departures"),
            Icons.Rounded.NorthEast,
            Icons.Outlined.NorthEast,
            StringRes.get("nav_icon_departures"),
            Routes.DEPARTURES
        ),
        NavItem(
            StringRes.get("nav_arrivals"),
            Icons.Rounded.SouthWest,
            Icons.Outlined.SouthWest,
            StringRes.get("nav_icon_arrivals"),
            Routes.ARRIVALS
        ),
        NavItem(
            StringRes.get("nav_station_info"),
            Icons.Rounded.Info,
            Icons.Outlined.Info,
            StringRes.get("nav_icon_station_info"),
            Routes.STATION_INFO
        ),
        NavItem(
            StringRes.get("nav_schedule"),
            Icons.Rounded.Schedule,
            Icons.Outlined.Schedule,
            StringRes.get("nav_icon_schedule"),
            Routes.SCHEDULE
        )
    )

    NavigationBar {
        navItems.forEach { item ->
            var isSelected by remember { mutableStateOf(false) }

            NavigationBarItem (
                label = { Text(item.label) },
                icon = {
                    if(isSelected)
                        Icon( item.selectedIcon, contentDescription = item.iconDescriptor )
                    else
                        Icon( item.unselectedIcon, contentDescription = item.iconDescriptor )
               },
                selected = isSelected,
                onClick = {
                    isSelected = !isSelected
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
