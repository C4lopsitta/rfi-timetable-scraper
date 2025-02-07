package cc.atomtech.timetable.components.navbars

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import cc.atomtech.timetable.Page

enum class NavType {
    Home,
    Station,
    Notices
}

@Composable
fun getNavType(navController: NavHostController): NavType {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    return when(currentDestination?.route) {
        Page.Home.route -> NavType.Home
        Page.Settings.route -> NavType.Home
        in listOf(
            Page.Station.Departures.route,
            Page.Station.Arrivals.route,
            Page.Station.Schedule.route,
            Page.Station.Info.route
        ) -> NavType.Station
        in listOf(
            Page.Notices.Trenitalia.route,
            Page.Notices.Live.route,
            Page.Notices.Announcements.route
        ) -> NavType.Notices
        else -> NavType.Home
    }
}

@Composable
fun NewNavigationBar(
    navController: NavHostController
) {
    when(getNavType(navController)) {
        NavType.Home -> TODO()
        NavType.Station -> TODO()
        NavType.Notices -> TODO()
    }
}