package cc.atomtech.rfi_timetable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    var trains by remember { mutableStateOf<Timetable?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        try {
            trains = RfiScraper.getStationTimetable(1079)
            loading = false
        } catch (e: Exception) {
            println(e.printStackTrace())
            error = e.toString()
        }
    }

    if(error != null) {
        AlertDialog(
            title = { Text("Data fetch error") },
            text = { Text(error.toString()) },
            buttons = {
                ElevatedButton(
                    content = { Text("Ok") },
                    onClick = {
                        error = null
                    }
                )
            },
            onDismissRequest = {
                error = null
            }
        )
    }

    val colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    MaterialTheme (
        colorScheme = colorScheme
    ) {
        Scaffold (
            topBar = { TopAppBar(
                title = { Text(trains?.stationName ?: "Timetables" ) },
                navigationIcon = {
                    Surface ( modifier = Modifier.padding(PaddingValues(12.dp)) ) {
                        Icon(Icons.Rounded.Train, contentDescription = "Train")
                    }
                }
            ) },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(label = { Text("Timetable") },
                        icon = { Icon(Icons.Rounded.Schedule, contentDescription = "Train") },
                        selected = true,
                        onClick = {

                        })
                    NavigationBarItem(label = { Text("Favourite Stations") },
                        icon = { Icon(Icons.Rounded.Star, contentDescription = "Star") },
                        selected = false,
                        onClick = {

                        })
                }
            }
        ) { paddingValues ->
            Surface (
                modifier = Modifier.padding(
                    top = paddingValues.calculateTopPadding(),
                    start = 12.dp
                ).fillMaxWidth()
            ) {
                if(!loading) {
                    LazyColumn(contentPadding = PaddingValues(0.dp, 12.dp, 12.dp, 12.dp)) {
                        items(trains?.departures ?: listOf<TrainData>()) { train ->
                            train.mobileRow()
                            Divider(
                                color = colorScheme.onSurface.copy(alpha = 0.1f),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                } else {
                    Surface (
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(PaddingValues(64.dp))
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

        }
    }
}

