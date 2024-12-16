package cc.atomtech.timetable.components.navbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Announcement
import androidx.compose.material.icons.automirrored.outlined.Announcement
import androidx.compose.material.icons.automirrored.rounded.Announcement
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Announcement
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocationCity
import androidx.compose.material.icons.rounded.Settings
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
fun InfoLavoriNavBar(navController: NavHostController) {
    val navItems = listOf(
        NavItem(
            StringRes.get("nav_home"),
            Icons.Rounded.Home,
            Icons.Outlined.Home,
            StringRes.get("nav_icon_home"),
            Routes.HOME_DEPARTURES
        ),
        NavItem(
            StringRes.get("nav_station"),
            Icons.Rounded.LocationCity,
            Icons.Outlined.LocationCity,
            StringRes.get("nav_icon_station"),
            Routes.STATION
        ),
        NavItem(
            StringRes.get("nav_notices"),
            Icons.AutoMirrored.Rounded.Announcement,
            Icons.AutoMirrored.Outlined.Announcement,
            StringRes.get("nav_icon_station"),
            Routes.NOTICES
        ),
        NavItem(
            StringRes.get("nav_settings"),
            Icons.Rounded.Settings,
            Icons.Outlined.Settings,
            StringRes.get("nav_icon_station"),
            Routes.SETTINGS
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
