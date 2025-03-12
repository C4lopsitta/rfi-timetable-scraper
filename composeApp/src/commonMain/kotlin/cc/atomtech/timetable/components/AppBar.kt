package cc.atomtech.timetable.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.models.viewmodels.Station

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavHostController,
           stationData: Station,
           triggerReload: () -> Unit) {
    TopAppBar(
        title = {
            Text(stationData.currentStation.value?.name ?: StringRes.get("app_name"))
        },
        navigationIcon = {
            Surface() {
                if (navController.currentBackStackEntryAsState().value?.destination?.route?.contains(
                        "details/"
                    ) == true
                ) {
                    IconButton(content = {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = StringRes.get("back")
                        )
                    },
                        onClick = { navController.popBackStack() })
                } else  {
                    Icon(
                        Icons.Rounded.Train,
                        contentDescription = StringRes.get("app_name"),
                        modifier = Modifier.padding( horizontal = 12.dp )
                    )
                }
            }
        },
        actions = {
            IconButton(
                content = {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = StringRes.get("search")
                    )
                },
                onClick = {
                    navController.navigate("search")
                }
            )
            IconButton(
                content = {
                    Icon(
                        Icons.Rounded.Refresh,
                        contentDescription = StringRes.get("reload")
                    )
                },
                onClick = { triggerReload() }
            )
        }
    )
}