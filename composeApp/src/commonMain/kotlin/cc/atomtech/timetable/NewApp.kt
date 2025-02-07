package cc.atomtech.timetable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Announcement
import androidx.compose.material.icons.rounded.Announcement
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.NorthEast
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SouthWest
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Train
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
import cc.atomtech.timetable.components.LargeIconText
import cc.atomtech.timetable.components.navbars.HomeNavBar
import cc.atomtech.timetable.components.navbars.NewNavigationBar
import cc.atomtech.timetable.views.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

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
                NewNavigationBar (
                    navController = navController
                )
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = Page.Home.route
            ) {
                composable (Page.Home.route) {
                    LargeIconText(
                        icon = Icons.Rounded.Home,
                        text = "Home"
                    )
                }

                navigation (
                    route = Page.Station.route,
                    startDestination = Page.Station.Departures.route
                ) {
                    composable (Page.Station.Departures.route) {
                        LargeIconText(
                            icon = Icons.Rounded.NorthEast,
                            text = "Station -> Departures"
                        )
                    }
                    composable (Page.Station.Arrivals.route) {
                        LargeIconText(
                            icon = Icons.Rounded.SouthWest,
                            text = "Station -> Arrivals"
                        )
                    }
                    composable (Page.Station.Schedule.route) {
                        LargeIconText(
                            icon = Icons.Rounded.Schedule,
                            text = "Station -> Schedule"
                        )
                    }
                    composable (Page.Station.Arrivals.route) {
                        LargeIconText(
                            icon = Icons.Rounded.Info,
                            text = "Station -> Info"
                        )
                    }
                }

                navigation (
                    route = Page.Notices.route,
                    startDestination = Page.Notices.Trenitalia.route
                ) {
                    composable (Page.Notices.Trenitalia.route) {
                        LargeIconText(
                            icon = Icons.Rounded.Train,
                            text = "Notices -> Trenitalia"
                        )
                    }
                    composable (Page.Notices.Live.route) {
                        LargeIconText(
                            icon = Icons.Rounded.Timer,
                            text = "Notices -> Live"
                        )
                    }
                    composable (Page.Notices.Announcements.route) {
                        LargeIconText(
                            icon = Icons.AutoMirrored.Rounded.Announcement,
                            text = "Notices -> Announcements"
                        )
                    }
                }

                composable (Page.Settings.route) {
                    LargeIconText(
                        icon = Icons.Rounded.Settings,
                        text = "Settings"
                    )
                }
            }
        }
    }
}
