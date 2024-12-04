package cc.atomtech.timetable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavHostController
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
    Column (
        modifier = Modifier.padding( all = 12.dp )
    ) {
        Text("Hello, World!")
        Text("As we stated before, the new UI is being developed! Come back on the next update and check out what's new!")
        OutlinedButton(
            content = { Text("Go back to previous UI.") },
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
