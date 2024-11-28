package cc.atomtech.timetable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.compose.rememberNavController


@Composable
actual fun storePreferences(): DataStore<Preferences> {
    return remember {
        instantiatePreferences(
            createPath = {
                "${System.getProperty("user.home")}/.timetables/$preferencesFile"
            }
        )
    }
}

@Composable actual fun isNetworkAvailable(): Boolean {
    return true
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Timetables"
    ) {
        val navController = rememberNavController()
        Main(navController, isDesktop = true, androidContext = this)
    }
}