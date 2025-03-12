package cc.atomtech.timetable.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BookmarkRemove
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.components.LargeIconText
import cc.atomtech.timetable.models.viewmodels.Station

@Composable
fun BookmarkedStations(
    stationData: Station
) {
    val bookmarkedStations = derivedStateOf {
        stationData.allStationData.value.filter { it.isBookmarked }
    }

    if(bookmarkedStations.value.isNotEmpty()) {
        LazyColumn (
            modifier = Modifier.fillMaxSize().padding( end = 12.dp )
        ) {
            item {
                Text(StringRes.get("bookmarked_station"))
            }
            items(bookmarkedStations.value) { station ->
                ListItem(
                    headlineContent = { Text(station.name) },
                    supportingContent = { Text(station.stationCountry.toString()) },
                    trailingContent = {
                        IconButton(
                            content = { Icon(Icons.Rounded.BookmarkRemove, contentDescription = null) },
                            onClick = {
                                stationData.allStationData.value.find { it.id == station.id }?.isBookmarked = false
                            }
                        )
                    },
                    modifier = Modifier.clickable(
                        onClick = {
                            stationData.updateStation(station)
                        }
                    )
                )
            }
        }
    } else {
        LargeIconText(
            icon = Icons.Rounded.Bookmarks,
            text = StringRes.get("favourites_details")
        )
    }
}
