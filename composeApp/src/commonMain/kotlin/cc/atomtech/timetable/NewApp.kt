package cc.atomtech.timetable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navigation
import cc.atomtech.timetable.components.navbars.HomeNavBar
import cc.atomtech.timetable.components.navbars.NavSwitcher
import cc.atomtech.timetable.views.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@Composable
fun NewMain(navController: NavHostController,
            isDesktop: Boolean = false,
            colorScheme: ColorScheme? = null,
            preferences: AppPreferences) {

    val actualColorScheme = colorScheme
        ?: if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    MaterialTheme(
        colorScheme = actualColorScheme
    ) {
        Scaffold (
            topBar = {

            },
            bottomBar = {
                NavSwitcher( navController = navController )
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = Routes.HOME_DEPARTURES
            ) {
                composable(route = Routes.HOME_DEPARTURES) {  }
                composable(route = Routes.SEARCH) {  }
                composable(route = Routes.SETTINGS) {  }
                composable(route = Routes.FAVOURITES) {  }
                navigation(
                    route = Routes.STATION,
                    startDestination = Routes.DEPARTURES
                ) {
                    composable(route = Routes.DEPARTURES) {
                        Text("DEPARTURES")
                    }
                    composable(route = Routes.ARRIVALS) {
                        Text("ARRIVALS")
                    }
                    composable(route = Routes.STATION_INFO) {
                        Text("INFO")
                    }
                    composable(route = Routes.SCHEDULE) {
                        Text("SCHEDULE")
                    }
                    composable(route = Routes.TRAIN_DETAILS) {  }
                }
                navigation(
                    route = Routes.INFOLAVORI,
                    startDestination = Routes.TRENITALIA
                ) {
                    composable(route = Routes.TRENITALIA) {  }
                    composable(route = Routes.LIVE) {  }
                    composable(route = Routes.NOTICES) {  }
                }
            }
        }
    }
}
