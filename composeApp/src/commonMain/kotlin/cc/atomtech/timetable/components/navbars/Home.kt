package cc.atomtech.timetable.components.navbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocationCity
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
fun HomeNavBar(navController: NavHostController) {
    val navItems = listOf(
        NavItem(
            StringRes.get("nav_home"),
            { Icon(Icons.Rounded.Home, StringRes.get("nav_icon_home")) },
            { Icon(Icons.Filled.Home, StringRes.get("nav_icon_home")) },
            Routes.HOME
        ),
        NavItem(
            StringRes.get("nav_station"),
            { Icon( Icons.Rounded.LocationCity, StringRes.get("nav_icon_station") ) },
            { Icon( Icons.Filled.LocationCity, StringRes.get("nav_icon_station") ) },
            Routes.STATION
        ),
        NavItem(
            StringRes.get("nav_notices"),
            { Icon( Icons.Rounded.LocationCity, StringRes.get("nav_icon_station") ) },
            { Icon( Icons.Filled.LocationCity, StringRes.get("nav_icon_station") ) },
            Routes.NOTICES
        ),
        NavItem(
            StringRes.get("nav_settings"),
            { Icon( Icons.Rounded.LocationCity, StringRes.get("nav_icon_station") ) },
            { Icon( Icons.Filled.LocationCity, StringRes.get("nav_icon_station") ) },
            Routes.SETTINGS
        )
    )


    NavigationBar {
        navItems.forEach { item ->
            var isSelected by remember { mutableStateOf(false) }

            NavigationBarItem (
                label = { Text(item.label) },
                icon = { if(isSelected) item.selectedIcon else item.unselectedIcon },
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
