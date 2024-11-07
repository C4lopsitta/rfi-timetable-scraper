package cc.atomtech.rfi_timetable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
    var trains by remember { mutableStateOf(listOf<TrainData>()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            trains = RfiScraper.getStationTimetable(1079)
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

    MaterialTheme (
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold (
            topBar = { TopAppBar(
                title = { Text("Timetables") },
                navigationIcon = {
                    Surface ( modifier = Modifier.padding(PaddingValues(12.dp)) ) {
                        Icon(Icons.Rounded.Train, contentDescription = "Train")
                    }
                }
            ) }
        ) { paddingValues ->
            Surface (
                modifier = Modifier.padding(
                    top = paddingValues.calculateTopPadding(),
                    start = 12.dp
                )
            ) {
                LazyColumn ( contentPadding = PaddingValues(12.dp) ) {
                    items(trains) { train ->
                        train.build()
                        Divider()
                    }
                }
            }

        }
    }
}

