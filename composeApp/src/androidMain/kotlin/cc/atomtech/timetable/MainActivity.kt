package cc.atomtech.timetable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            val navController = rememberNavController()
            Main(navController)
        }
    }
}
