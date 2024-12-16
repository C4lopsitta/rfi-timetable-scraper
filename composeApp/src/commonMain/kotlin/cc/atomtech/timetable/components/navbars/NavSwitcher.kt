package cc.atomtech.timetable.components.navbars

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import cc.atomtech.timetable.Routes

@Composable
fun NavSwitcher(
    navController: NavHostController
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    if (currentRoute?.startsWith(Routes.STATION) == true) StationNavBar( navController = navController )
    else if (currentRoute?.startsWith(Routes.INFOLAVORI) == true) InfoLavoriNavBar( navController = navController )
    else HomeNavBar( navController = navController )
}
