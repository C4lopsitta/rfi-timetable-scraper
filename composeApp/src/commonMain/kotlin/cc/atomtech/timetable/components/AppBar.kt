package cc.atomtech.timetable.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.models.TimetableState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavHostController,
           searchQuery: String,
           timetable: TimetableState?,
           triggerReload: () -> Unit,
           updateSearchQuery: (String) -> Unit,
           resetSearchSuggestions: () -> Unit) {
    TopAppBar(
        title = {
            if (navController.currentBackStackEntryAsState().value?.destination?.route?.contains("search") == false) {
                Text(timetable?.stationName ?: StringRes.get("app_name"))
            } else if (navController.currentBackStackEntryAsState().value?.destination?.route?.contains(
                    ""
                ) == false
            ) {
                Text(StringRes.get("top_strikes_maintenances"))
            } else {
                TextField(
                    value = searchQuery,
                    onValueChange = { updateSearchQuery(it) },
                    placeholder = { Text(StringRes.get("station_search_placeholder")) },
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    shape = TextFieldDefaults.OutlinedTextFieldShape,
                )
            }
        },
        navigationIcon = {
            Surface(
                modifier = Modifier.padding(
                    PaddingValues(
                        if (navController.currentBackStackEntryAsState().value?.destination?.route?.contains(
                                "search"
                            ) == false
                        ) 12.dp else 0.dp
                    )
                )
            ) {
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
                } else if (navController.currentBackStackEntryAsState().value?.destination?.route?.contains(
                        "search"
                    ) == false
                ) {
                    Icon(Icons.Rounded.Train, contentDescription = StringRes.get("app_name"))
                } else {
                    IconButton(content = {
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = StringRes.get("close_search")
                        )
                    },
                        onClick = { resetSearchSuggestions() })
                }
            }
        },
        actions = {
            IconButton(content = {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = StringRes.get("search")
                )
            },
                onClick = {
                    navController.navigate("search")
                })
            IconButton(content = {
                Icon(
                    Icons.Rounded.Refresh,
                    contentDescription = StringRes.get("reload")
                )
            },
                onClick = { triggerReload() })
        }
    )
}