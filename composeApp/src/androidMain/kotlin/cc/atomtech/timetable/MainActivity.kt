package cc.atomtech.timetable

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.compose.rememberNavController

@Composable
actual fun storePreferences(): DataStore<Preferences> {
    val context = LocalContext.current
    return remember {
        instantiatePreferences(
            createPath = {
                context.filesDir.resolve(preferencesFile).absolutePath
            },
        )
    }
}

@Composable
actual fun isNetworkAvailable(): Boolean {
    val context = LocalContext.current

    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

class MainActivity : ComponentActivity() {
    @Composable
    fun isTablet(): Boolean {
        StringRes.context = LocalContext.current

        val configuration = LocalConfiguration.current
        return if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            configuration.screenWidthDp > 840
        } else {
            configuration.screenWidthDp > 600
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val isTablet = isTablet()

            val context = LocalContext.current
            val colorScheme = if (isSystemInDarkTheme()) {
                dynamicDarkColorScheme(context)
            } else dynamicLightColorScheme(context)

            Main(navController = navController,
                isDesktop = isTablet,
                colorScheme = colorScheme)
        }
    }
}
