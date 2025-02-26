package cc.atomtech.timetable

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.first



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

//
//@Composable
//actual fun isNetworkAvailable(): Boolean {
//    val context = LocalContext.current
//
//    val connectivityManager =
//        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    val networkInfo = connectivityManager.activeNetworkInfo
//    return networkInfo != null && networkInfo.isConnected
//}

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

    companion object {
        lateinit var instance: MainActivity
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this

        setContent {
            val navController = rememberNavController()
            val isTablet = isTablet()

            val context = LocalContext.current
            val colorScheme = if (isSystemInDarkTheme()) {
                dynamicDarkColorScheme(context)
            } else dynamicLightColorScheme(context)

            val preferences = AppPreferences(storePreferences())

            Main(
                navController = navController,
                preferences = preferences,
                isDesktop = isTablet,
                colorScheme = colorScheme
            )
        }
    }
}
