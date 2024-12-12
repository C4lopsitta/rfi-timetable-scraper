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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import cc.atomtech.timetable.components.navbars.HomeNavBar
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
        NavHost(
            navController = navController,
            startDestination = Routes.HOME
        ) {
            composable (
                route = Routes.HOME,
//                startDestination = Routes.DEPARTURES
            ) {
                Scaffold(
                    bottomBar = { HomeNavBar(navController = navController) }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.DEPARTURES
                    ) {
                        composable(route = Routes.DEPARTURES) {
                            Column(
                                modifier = Modifier.padding(all = 12.dp)
                            ) {
                                Text("Hello, World!", fontSize = 24.sp)
                                Text("As we stated before, the new UI is being developed! Come back on the next update and check out what's new!")
                                OutlinedButton(
                                    content = { Text("Go back to previous UI") },
                                    onClick = {
                                        CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
                                            preferences.setUseNewUi(false)
                                            exitProcess(0)
                                        }
                                    }
                                )
                                Text("Click the button above, then restart the app to go back.")
                            }
                        }
                    }
                    composable(route = Routes.SETTINGS) {
                        Settings(preferences = preferences)
                    }
                }
            }

            composable( route = Routes.STATION ) {  }
            composable( route = Routes.NOTICES ) {  }

        }
    }
}
