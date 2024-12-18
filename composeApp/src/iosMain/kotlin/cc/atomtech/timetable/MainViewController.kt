package cc.atomtech.timetable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.compose.rememberNavController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun storePreferences(): DataStore<Preferences> {
    return remember {
        instantiatePreferences {
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )

            requireNotNull(documentDirectory).path + "/timetables-prefs.preferences_pb"
        }
    }
}

fun MainViewController() = ComposeUIViewController {
    val navController = rememberNavController()
    val prefsFile = storePreferences()
    var preferences: AppPreferences? = null

    try {
        preferences = AppPreferences(prefsFile)
    } catch (e: Exception) {
        println(e.printStackTrace())
    }
    throw Exception("Banana")

    Main(
        navController = navController,
        preferences = preferences!!
    )
}
